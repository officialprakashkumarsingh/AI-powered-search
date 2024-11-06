import React from 'react';

interface SuggestedQuery {
  icon: string;
  text: string;
}

interface SuggestedQueriesProps {
  queries: SuggestedQuery[];
  onQuerySelect: (text: string) => void;
}

export function SuggestedQueries({ queries, onQuerySelect }: SuggestedQueriesProps) {
  return (
    <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
      {queries.map((item, index) => (
        <button
          key={index}
          onClick={() => onQuerySelect(item.text)}
          className="flex items-center gap-3 bg-[#2A2A2A] p-4 rounded-xl text-left hover:bg-[#333]"
        >
          <span>{item.icon}</span>
          <span>{item.text}</span>
        </button>
      ))}
    </div>
  );
}