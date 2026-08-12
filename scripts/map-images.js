// /tmp/imgmap.tsv (filename \t url) padhta hai aur menu-firestore.json me
// har item ko sabse relevant photo se map karta hai. menuVersion bump.
const fs = require("fs");

const norm = (s) => s.toLowerCase().replace(/\.[^.]+$/, "").replace(/[^a-z0-9]+/g, "_").replace(/^_|_$/g, "");

const rules = [
  [/thali/i, "thali"],
  [/paneer.*steam|steam.*paneer/i, "paneer_steam_momos"],
  [/paneer.*fried/i, "paneer_fried_momos"],
  [/kurkure/i, "kurkure_momos"],
  [/veg.*steam|steam momo/i, "veg_steam_momo"],
  [/fried momo/i, "veg_freid_momos"],
  [/momo/i, "veg_steam_momo"],
  [/chilly garlic/i, "chilly_garlic_noolde"],
  [/noodle|chowmein/i, "noodles"],
  [/manchurian/i, "manchurian"],
  [/chilly paneer/i, "chilly_paneer"],
  [/potato|fries/i, "chilly_potato"],
  [/chaap|chap/i, "chaap"],
  [/shahi paneer|shahi kajui/i, "shahi_paneer"],
  [/paneer/i, "shahi_paneer"],
  [/dal|makhni|makhani/i, "dal_makhni"],
  [/burger|roll/i, "burger"],
  [/naan|roti|paratha|pararha/i, "roti"],
  [/rice|pulav|biryani|briyani|jeera|chawal/i, "rice"],
  [/raita|salad|papad|peanut/i, "raita"],
  [/thali/i, "thali"],
];
const catFallback = { 1:"noodles",2:"veg_steam_momo",3:"chaap",4:"manchurian",5:"rice",6:"burger",7:"dal_makhni",8:"roti",9:"shahi_paneer",10:"raita",11:"rice",12:"thali" };

const url = {};
for (const line of fs.readFileSync("/tmp/imgmap.tsv", "utf8").split("\n")) {
  if (!line.trim()) continue;
  const [file, u] = line.split("\t");
  if (u && u.startsWith("http")) url[norm(file)] = u.trim();
}
console.log("Loaded", Object.keys(url).length, "image URLs");

const doc = JSON.parse(fs.readFileSync("menu-firestore.json", "utf8"));
doc.menuVersion = (doc.menuVersion || 3) + 1;
let mapped = 0, fb = 0, miss = 0;
for (const c of doc.categories) {
  for (const it of c.items) {
    const hit = rules.find(([re]) => re.test(it.name));
    let key = hit && url[hit[1]] ? hit[1] : null;
    if (!key && url[catFallback[c.id]]) key = catFallback[c.id];
    if (key) { it.imageUrl = url[key]; hit ? mapped++ : fb++; }
    else { it.imageUrl = ""; miss++; }
  }
}
fs.writeFileSync("menu-firestore.json", JSON.stringify(doc, null, 2));
console.log(`✅ menuVersion=${doc.menuVersion} | keyword: ${mapped}, category-fallback: ${fb}, empty: ${miss}`);
console.log("👉 Ab chalao: node upload-menu.js");
