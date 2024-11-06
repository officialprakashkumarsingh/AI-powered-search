import React, { useState } from 'react';
import { GoogleGenerativeAI } from '@google/generative-ai';
import { SearchBar } from './components/SearchBar';
import { SuggestedQueries } from './components/SuggestedQueries';
import { Footer } from './components/Footer';

const genAI = new GoogleGenerativeAI('AIzaSyCu5ss8-UrawlqgjaVJjbK6Z0o6hnzeS7g');

const suggestedQueries = [
  { icon: '🔴', text: 'What is red light therapy?' },
  { icon: '👩‍🍳', text: 'Bestselling cookbooks this year' },
  { icon: '📈', text: 'Will US interest rates go down this year?' },
  { icon: '🗳️', text: 'Registration deadlines for the US election' }
];

function App() {
  const [query, setQuery] = useState('');
  const [response, setResponse] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSearch = async () => {
    if (!query.trim()) return;
    
    setLoading(true);
    try {
      const model = genAI.getGenerativeModel({ model: "gemini-pro" });
      const result = await model.generateContent(query);
      const response = await result.response;
      setResponse(response.text());
    } catch (error) {
      setResponse('Sorry, there was an error processing your request.');
    } finally {
      setLoading(false);
    }
  };

  const handleQuerySelect = (text: string) => {
    setQuery(text);
    handleSearch();
  };

  return (
    <div className="min-h-screen bg-[#1C1C1C] text-white">
      <main className="max-w-3xl mx-auto px-4 pt-16 pb-20">
        <div className="text-center mb-12">
          <h1 className="text-5xl font-bold mb-4">Know it all</h1>
          <p className="text-xl text-gray-400">Discover answers to everything with AI-powered search</p>
        </div>
        
        <div className="w-full">
          <SearchBar
            query={query}
            onQueryChange={setQuery}
            onSearch={handleSearch}
          />

          {(loading || response) && (
            <div className="mt-4 bg-[#2A2A2A] p-6 rounded-xl">
              {loading ? (
                <div className="flex justify-center">
                  <div className="w-6 h-6 border-2 border-t-white border-white/20 rounded-full animate-spin"></div>
                </div>
              ) : (
                <p className="whitespace-pre-wrap">{response}</p>
              )}
            </div>
          )}

          <div className="mt-8">
            <SuggestedQueries
              queries={suggestedQueries}
              onQuerySelect={handleQuerySelect}
            />
          </div>
        </div>
      </main>
      <Footer />
    </div>
  );
}

export default App;