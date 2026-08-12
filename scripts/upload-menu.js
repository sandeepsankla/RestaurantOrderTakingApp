// menu-firestore.json ko Firestore ke `menus/default` document me upload karta hai.
// Ye file already sahi shape me hai (categories + menuVersion), koi transform nahi.
//
// Setup (ek baar):
//   cd scripts
//   npm init -y && npm install firebase-admin
//   Firebase Console > Project Settings > Service Accounts > Generate new private key
//     -> download karke isi scripts/ folder me "serviceAccount.json" naam se rakho
//   (serviceAccount.json ko .gitignore me daalo — ye SECRET hai)
//
// Menu badalna ho:
//   scripts/menu-firestore.json edit karo (item add/price change), aur
//   usme "menuVersion" ko +1 badhao (warna app cached menu dikhायega).
//
// Chalao:
//   node upload-menu.js

const { initializeApp, cert } = require("firebase-admin/app");
const { getFirestore } = require("firebase-admin/firestore");

initializeApp({
  credential: cert(require("./serviceAccount.json")),
});
const db = getFirestore();

const menuDoc = require("./menu-firestore.json");

db.collection("menus")
  .doc("default")
  .set(menuDoc)
  .then(() => {
    const items = menuDoc.categories.reduce((n, c) => n + c.items.length, 0);
    console.log(
      `✅ Uploaded menuVersion=${menuDoc.menuVersion}: ${menuDoc.categories.length} categories, ${items} items`
    );
    process.exit(0);
  })
  .catch((e) => {
    console.error("❌ Upload failed:", e.message);
    process.exit(1);
  });
