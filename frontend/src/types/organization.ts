export type Organization = {
  id: number; // Corresponds to @JoinColumn(name = "id") which links to Organization's PK
  // Add other relevant Organization fields if your API returns them and you need them
  name?: string; // Example: often organizations have names
};
