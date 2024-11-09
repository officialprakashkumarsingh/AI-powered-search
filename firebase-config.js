import { initializeApp } from "https://www.gstatic.com/firebasejs/10.8.0/firebase-app.js";
import { 
    getFirestore, 
    collection, 
    addDoc, 
    serverTimestamp 
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

// Create a function to handle database storage
async function storeSearchData(query, response) {
    try {
        console.log('Attempting to store data:', { query, response });
        
        const searchData = {
            query: query,
            response: response,
            timestamp: serverTimestamp(),
            userAgent: navigator.userAgent,
            createdAt: new Date().toISOString()
        };

        const collectionRef = collection(db, 'searches');
        const docRef = await addDoc(collectionRef, searchData);
        
        console.log("Success! Document written with ID:", docRef.id);
        console.log("Stored data:", searchData);
        return true;
    } catch (e) {
        console.error("Error adding document:", e);
        console.error("Error details:", e.message);
        return false;
    }
}

export { db, storeSearchData }; 