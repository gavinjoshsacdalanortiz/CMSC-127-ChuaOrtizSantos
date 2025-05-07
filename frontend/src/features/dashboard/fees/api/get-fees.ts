import { useState, useEffect, useCallback, useMemo } from "react";
import type { Fee, FeeQueryOptions, UseFeesReturn } from "@/types/fee"; // Adjust path as needed
import { api } from "@/lib/api-client";

export function useFees(options: FeeQueryOptions = {}): UseFeesReturn {
  const [fees, setFees] = useState<Fee[] | null>(null);
  const [pending, setPending] = useState<boolean>(false);
  const [error, setError] = useState<Error | null>(null);
  const [refreshIndex, setRefreshIndex] = useState<number>(0);

  // useMemo to stringify options to prevent unnecessary re-renders
  // if the options object is re-created with the same values.
  const optionsString = useMemo(() => JSON.stringify(options), [options]);

  useEffect(() => {
    const currentOptions = JSON.parse(optionsString) as FeeQueryOptions;
    console.log("useFees Effect: Fetching with options ->", currentOptions);

    let isCancelled = false;

    const fetchData = async () => {
      setPending(true);
      setError(null);

      // Prepare query parameters
      // The 'isPaid' boolean needs to be translated into something the backend understands.
      // For example, if backend expects 'datePaidIsNull=true' or 'status=PAID/UNPAID'.
      // Let's assume for now your backend can directly interpret 'isPaid' or you'll adjust.
      // If your backend expects 'organizationId' but your Java Fee has 'organization' object,
      // your Spring controller should handle mapping 'organizationId' query param to filter by Organization.
      const params: Record<string, any> = { ...currentOptions };

      // Example: If backend expects a specific param for paid status instead of 'isPaid'
      // if (typeof currentOptions.isPaid === 'boolean') {
      //   params.paidStatus = currentOptions.isPaid ? 'PAID' : 'UNPAID';
      //   delete params.isPaid; // remove the original isPaid if it's not directly used
      // }

      try {
        // Assuming your backend returns an object like { fees: Fee[] }
        // or if it returns Fee[] directly, change to api.get<Fee[]>
        const response = await api.get<{ fees: Fee[] }>( // Or just `Fee[]` if your API returns an array directly
          "/fees", // Your Spring Boot endpoint for fees
          {
            params: params, // Pass the processed params
          },
        );

        if (!isCancelled) {
          // If API returns { fees: [...] }, use response.fees
          // If API returns [...] directly, use response directly
          setFees(response.fees);
        }
      } catch (err) {
        console.error("useFees: Fetch failed", err);
        if (!isCancelled) {
          setError(
            err instanceof Error ? err : new Error("Failed to fetch fees"),
          );
          setFees(null); // Clear fees on error
        }
      } finally {
        if (!isCancelled) {
          setPending(false);
        }
      }
    };

    fetchData();

    // Cleanup function
    return () => {
      isCancelled = true;
      console.log("useFees Effect: Cleanup");
    };
  }, [optionsString, refreshIndex]); // Re-run effect if options or refreshIndex changes

  const refresh = useCallback(() => {
    setRefreshIndex((prevIndex) => prevIndex + 1);
  }, []);

  return {
    fees,
    pending,
    error,
    setError,
    refresh,
  };
}
