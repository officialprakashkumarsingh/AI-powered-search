// Simple encryption/decryption using base64
function encrypt(text) {
    return btoa(text);
}

function decrypt(encoded) {
    return atob(encoded);
}

window.encrypt = encrypt;
window.decrypt = decrypt; 