import React from 'react';
import { ArrowRight } from 'lucide-react';

interface SearchBarProps {
  query: string;
  onQueryChange: (value: string) => void;
  onSearch: () => void;
}

export function SearchBar({ query, onQueryChange, onSearch }: SearchBarProps) {
  const handleKeyPress = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter') {
      onSearch();
    }
  };

  return (
    <div className="flex items-center bg-[#2A2A2A] rounded-xl p-4">
      <input
        type="text"
        value={query}
        onChange={(e) => onQueryChange(e.target.value)}
        onKeyPress={handleKeyPress}
        placeholder="Search anything..."
        className="flex-1 bg-transparent outline-none text-lg text-white"
      />
      <button
        onClick={onSearch}
        className="ml-4 text-white hover:text-gray-300"
      >
        <ArrowRight className="w-5 h-5" />
      </button>
    </div>
  );
}