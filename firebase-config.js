import { initializeApp } from "https://www.gstatic.com/firebasejs/10.8.0/firebase-app.js";
import { 
    getFirestore, 
    collection, 
    addDoc, 
    serverTimestamp,
    getDocs,
    query,
    orderBy 
} from "https://www.gstatic.com/firebasejs/10.8.0/firebase-firestore.js";

// Initialize Firebase
const firebaseConfig = {
    apiKey: "AIzaSyCMR3Fv-2zB7GfyRqmtLZusNqqY0VEUg4k",
    authDomain: "fir-8481c.firebaseapp.com",
    projectId: "fir-8481c",
    storageBucket: "fir-8481c.appspot.com",
    messagingSenderId: "292488648902",
    appId: "1:292488648902:web:83c8714d915ad7375e9fc7",
    measurementId: "G-DQ90J277V3"
};

// Initialize Firebase app
const app = initializeApp(firebaseConfig);
const db = getFirestore(app);

// Function to store search data
async function storeSearchData(query, response) {
    try {
        const docRef = await addDoc(collection(db, 'searches'), {
            query: query,
            response: response,
            timestamp: serverTimestamp(),
            userAgent: navigator.userAgent
        });
        return true;
    } catch (e) {
        console.error("Error adding document: ", e);
        return false;
    }
}

export { db, storeSearchData }; 