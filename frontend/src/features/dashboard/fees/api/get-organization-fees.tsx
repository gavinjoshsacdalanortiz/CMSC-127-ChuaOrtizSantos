import { useState, useEffect, useMemo } from "react";
import type { Fee, FeeQueryOptions, UseFeesReturn } from "@/types/fee";
import { api } from "@/lib/api-client";

// This hook is for fetching ALL organization fees, suitable for admin views
export function useOrganizationFees(
  options: FeeQueryOptions = {},
  organizationId: string,
  enabled: boolean = true // Crucial for tabbed interfaces
): UseFeesReturn {
  const [fees, setFees] = useState<Fee[] | null>(null);
  // availableOptions can be shared or fetched similarly if needed, or passed down from a parent component
  const [availableOptions, setAvailableOptions] = useState<UseFeesReturn["options"]>({
    availableYears: [],
  });
  const [pending, setPending] = useState<boolean>(false);
  const [error, setError] = useState<Error | null>(null);

  const memoizedOptions = useMemo(() => options, [JSON.stringify(options)]); // Stringify for deep comparison in useMemo dep array

  // Effect for fetching available years (could be shared or from a dedicated endpoint)
  useEffect(() => {
    if (!organizationId || !enabled) {
        setAvailableOptions({ availableYears: [] });
        return;
    }
    let isCancelled = false;
    // This should ideally be a dedicated endpoint for options to avoid fetching all fees just for years
    const fetchOrgOptions = async () => {
        try {
            // Example: Assuming an endpoint that returns all fees for year derivation
            // Or better: an endpoint like /organizations/{orgId}/fees/options
            const response = await api.get<Fee[]>(`/fees/organization/${organizationId}`); // ADMIN Endpoint
            if(!isCancelled) {
                const years = [...new Set(response.map((fee) => String(fee.year)))].sort((a, b) => parseInt(b) - parseInt(a));
                setAvailableOptions({ availableYears: years });
            }
        } catch (err) {
            console.error("useOrganizationFees: Failed to fetch available years", err);
            if (!isCancelled) setAvailableOptions({ availableYears: [] });
        }
    };
    fetchOrgOptions();
    return () => { isCancelled = true; };
  }, [organizationId, enabled]);


  // Effect for fetching fees based on options
  useEffect(() => {
    if (!organizationId || !enabled) {
      setFees(null); // Clear data if not enabled
      setPending(false);
      setError(null);
      return;
    }

    let isCancelled = false;
    const fetchData = async () => {
      setPending(true);
      setError(null);

      const queryParams = { ...memoizedOptions };
       // Ensure numeric values for semester and year if they are strings in options
      if (queryParams.semester && typeof queryParams.semester === 'string') {
        queryParams.semester = parseInt(queryParams.semester, 10);
      }
      if (queryParams.year && typeof queryParams.year === 'string') {
        queryParams.year = parseInt(queryParams.year, 10);
      }


      // Admin endpoint for all fees, with filters
      // Example: /fees/organization/{organizationId}/all/filter
      // Or: /fees/organization/{organizationId}/all (if filters are always applied via params)
      const endpoint = `/fees/organization/${organizationId}/filter`; // ADJUST THIS TO YOUR ADMIN ENDPOINT

      try {
        const response = await api.get<Fee[]>(
          endpoint,
          { params: queryParams } // Always send params, backend handles empty ones
        );
        if (!isCancelled) {
          setFees(response);
        }
      } catch (err) {
        console.error("useOrganizationFees: Fetch failed", err);
        if (!isCancelled) {
          setError(err instanceof Error ? err : new Error("Failed to fetch organization fees"));
          setFees(null);
        }
      } finally {
        if (!isCancelled) {
          setPending(false);
        }
      }
    };

    fetchData();

    return () => {
      isCancelled = true;
    };
  }, [memoizedOptions, organizationId, enabled]);

  return {
    fees,
    options: availableOptions,
    pending,
    error,
    setError,
  };
}