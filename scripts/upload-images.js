// 1) ~/Desktop/menu-images ki saari photos Firebase Storage pe upload karta hai (public)
// 2) menu-firestore.json me har item ko sabse relevant photo se map karta hai
// 3) menuVersion bump kar deta hai
// Uske baad: node upload-menu.js  (menu doc Firestore me push)

const { initializeApp, cert } = require("firebase-admin/app");
const { getStorage } = require("firebase-admin/storage");
const fs = require("fs");
const path = require("path");

const BUCKET = "restaurantordertakingapp.firebasestorage.app";
const SRC = "/Users/apple/Desktop/menu-images";

initializeApp({
  credential: cert(require("./serviceAccount.json")),
  storageBucket: BUCKET,
});
const bucket = getStorage().bucket();

const norm = (s) => s.toLowerCase().replace(/\.[^.]+$/, "").replace(/[^a-z0-9]+/g, "_").replace(/^_|_$/g, "");

// item-name keyword -> image key (upar wale se pehle match hota hai)
const rules = [
  [/paneer.*steam|steam.*paneer/i, "paneer_steam_momos"],
  [/paneer.*fried|fried.*paneer.*momo/i, "paneer_fried_momos"],
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
// category id -> fallback image key
const catFallback = {
  1: "noodles", 2: "veg_steam_momo", 3: "chaap", 4: "manchurian", 5: "rice",
  6: "burger", 7: "dal_makhni", 8: "roti", 9: "shahi_paneer", 10: "raita", 11: "rice", 12: "thali",
};

(async () => {
  // 1) upload all images
  const files = fs.readdirSync(SRC).filter((f) => /\.(png|jpe?g)$/i.test(f));
  const url = {};
  for (const f of files) {
    const key = norm(f);
    const dest = `menu-images/${key}.png`;
    await bucket.upload(path.join(SRC, f), {
      destination: dest,
      metadata: { cacheControl: "public,max-age=31536000" },
    });
    await bucket.file(dest).makePublic();
    url[key] = `https://storage.googleapis.com/${BUCKET}/${dest}`;
    console.log("⬆️  " + f + "  ->  " + key);
  }
  console.log(`\n✅ ${Object.keys(url).length} images uploaded to Storage\n`);

  // 2) map into menu-firestore.json
  const doc = JSON.parse(fs.readFileSync("menu-firestore.json", "utf8"));
  doc.menuVersion = (doc.menuVersion || 3) + 1;
  let mapped = 0, fb = 0, miss = 0;
  for (const c of doc.categories) {
    for (const it of c.items) {
      const hit = rules.find(([re]) => re.test(it.name));
      let key = hit && url[hit[1]] ? hit[1] : null;
      if (!key) key = url[catFallback[c.id]] ? catFallback[c.id] : null;
      if (key) { it.imageUrl = url[key]; hit ? mapped++ : fb++; }
      else { it.imageUrl = ""; miss++; }
    }
  }
  fs.writeFileSync("menu-firestore.json", JSON.stringify(doc, null, 2));
  console.log(`✅ menuVersion=${doc.menuVersion} | keyword-mapped: ${mapped}, category-fallback: ${fb}, empty: ${miss}`);
  console.log("\n👉 Ab chalao:  node upload-menu.js");
  process.exit(0);
})().catch((e) => { console.error("❌", e.message); process.exit(1); });
