import { useState, useEffect, useMemo } from "react";
import type { Fee, FeeQueryOptions, UseFeesReturn } from "@/types/fee"; // Adjust path as needed
import { api } from "@/lib/api-client";

export function useFees(
  options: FeeQueryOptions = {},
  organizationId: string
): UseFeesReturn {
  const [fees, setFees] = useState<Fee[] | null>(null);
  const [availableOptions, setAvailableOptions] = useState<
    UseFeesReturn["options"]
  >({
    availableYears: [],
  });

  const [pending, setPending] = useState<boolean>(false);
  const [error, setError] = useState<Error | null>(null);
  const optionsString = useMemo(() => JSON.stringify(options), [options]);

  useEffect(() => {
    (async () => {
      const response = await api.get<Fee[]>(
        `/fees/organization/${organizationId}`
      );

      const availableYears = [...new Set(response.map((fee) => fee.year))];

      console.log(availableYears);

      setAvailableOptions({
        availableYears: availableYears,
      });
    })();
  }, [fees]);

  useEffect(() => {
    let isCancelled = false;

    const fetchData = async () => {
      setPending(true);
      setError(null);

      if (optionsString.length > 0) {
        const currentOptions = JSON.parse(optionsString) as FeeQueryOptions;

        const params: Record<string, any> = { ...currentOptions };

        try {
          const response = await api.get<Fee[]>(
            `/fees/organization/${organizationId}/me/filter`,
            {
              params: params,
            }
          );

          if (!isCancelled) {
            setFees(response);
          }
        } catch (err) {
          console.error("useFees: Fetch failed", err);
          if (!isCancelled) {
            setError(
              err instanceof Error ? err : new Error("Failed to fetch fees")
            );
            setFees(null);
          }
        } finally {
          if (!isCancelled) {
            setPending(false);
          }
        }
      } else {
        try {
          const response = await api.get<Fee[]>(
            `/fees/organization/${organizationId}/me`
          );

          if (!isCancelled) {
            setFees(response);
          }
        } catch (err) {
          console.error("useFees: Fetch failed", err);
          if (!isCancelled) {
            setError(
              err instanceof Error ? err : new Error("Failed to fetch fees")
            );
            setFees(null);
          }
        } finally {
          if (!isCancelled) {
            setPending(false);
          }
        }
      }
    };

    fetchData();

    return () => {
      isCancelled = true;
      console.log("useFees Effect: Cleanup");
    };
  }, [optionsString, organizationId]);

  return {
    fees,
    options: availableOptions,
    pending,
    error,
    setError,
  };
}
