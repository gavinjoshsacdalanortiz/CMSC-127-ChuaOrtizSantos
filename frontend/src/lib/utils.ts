import { clsx, type ClassValue } from "clsx";
import { twMerge } from "tailwind-merge";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export function toTitleCase(str: string) {
  return str.replace(
    /\w\S*/g,
    (text) => text.charAt(0).toUpperCase() + text.substring(1).toLowerCase(),
  );
}

export function reverseMap<K, V>(map: Map<K, V>): Map<V, K> {
  const reversedMap: Map<V, K> = new Map();
  for (const [key, value] of map) {
    reversedMap.set(value, key);
  }
  return reversedMap;
}
