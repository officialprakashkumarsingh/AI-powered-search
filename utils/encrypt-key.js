import { encrypt } from './crypto.js';

const apiKey = 'YOUR_API_KEY_HERE';
console.log('Encrypted API key:', encrypt(apiKey)); 