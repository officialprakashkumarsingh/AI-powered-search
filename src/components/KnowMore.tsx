import React from 'react';
import { Brain, Zap, Globe, Clock } from 'lucide-react';

export function KnowMore() {
  const features = [
    {
      icon: <Brain className="w-8 h-8" />,
      title: "AI-Powered Insights",
      description: "Get intelligent answers powered by advanced AI technology"
    },
    {
      icon: <Zap className="w-8 h-8" />,
      title: "Lightning Fast",
      description: "Instant responses to all your queries in milliseconds"
    },
    {
      icon: <Globe className="w-8 h-8" />,
      title: "Vast Knowledge",
      description: "Access information from across the internet in one place"
    },
    {
      icon: <Clock className="w-8 h-8" />,
      title: "Real-time Updates",
      description: "Stay current with the latest information and updates"
    }
  ];

  return (
    <section className="w-full max-w-6xl mx-auto px-4 py-16">
      <h2 className="text-3xl font-bold text-center mb-12">Why Choose Us?</h2>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">
        {features.map((feature, index) => (
          <div
            key={index}
            className="flex flex-col items-center text-center p-6 bg-[#2A2A2A] rounded-xl hover:bg-[#333333] transition-all duration-300"
          >
            <div className="mb-4 text-purple-400">
              {feature.icon}
            </div>
            <h3 className="text-xl font-semibold mb-2">{feature.title}</h3>
            <p className="text-gray-400">{feature.description}</p>
          </div>
        ))}
      </div>
    </section>
  );
}