import { useState, useEffect, useMemo } from "react";
import type {
  Member,
  MemberQueryOptions,
  MembershipStatusPercentage,
  StatQueryOptions,
  UseMembersReturn,
} from "@/types/member";
import { api } from "@/lib/api-client";
import { getToken } from "@/lib/cookie";

export function useMembers(
  options: MemberQueryOptions = {},
  statOptions: StatQueryOptions = {},
  organizationId: string,
): UseMembersReturn {
  const [members, setMembers] = useState<Member[] | null>(null);
  const [stats, setStats] = useState<MembershipStatusPercentage[] | null>(null);
  const [availableOptions, setAvailableOptions] = useState<
    UseMembersReturn["options"]
  >({
    availableDegreePrograms: [],
    availableCommittees: [],
    availableBatches: [],
  });
  const [pending, setPending] = useState<boolean>(false);
  const [error, setError] = useState<Error | null>(null); // For main members data fetching
  const optionsString = useMemo(() => JSON.stringify(options), [options]);
  const statOptionsString = useMemo(
    () => JSON.stringify(statOptions),
    [statOptions],
  );

  useEffect(() => {
    const currentStatOptions = JSON.parse(
      statOptionsString,
    ) as StatQueryOptions;

    let isCancelled = false;

    const fetchData = async () => {
      setPending(true);
      setError(null);

      const token = getToken();
      if (!token) {
        setStats(null);
        setPending(false);
        return;
      }

      if (!organizationId) {
        setStats(null);
        setPending(false);
        return;
      }

      try {
        const responseData = await api.get<MembershipStatusPercentage[]>(
          `/membership/${organizationId}/members/status-percentages`,
          {
            params: currentStatOptions,
          },
        );

        if (!isCancelled) {
          setStats(responseData);
        }
      } catch (err) {
        console.error("useMembers: Fetch stat failed", err);
        if (!isCancelled) {
          setError(
            err instanceof Error ? err : new Error("Failed to fetch stats"),
          );
          setStats(null);
        }
      } finally {
        if (!isCancelled) {
          setPending(false);
        }
      }
    };

    if (organizationId) {
      fetchData();
    } else {
      setStats(null);
      setPending(false);
    }

    return () => {
      isCancelled = true;
    };
  }, [statOptionsString, organizationId]);

  useEffect(() => {
    if (
      !members ||
      !!availableOptions.availableDegreePrograms.length ||
      !!availableOptions.availableCommittees.length ||
      !!availableOptions.availableBatches.length
    )
      return;

    const availableDegrees = [
      ...new Set(members.map((member) => member.degreeProgram)),
    ];

    const availableCommittees = [
      ...new Set(
        members.map((member) => (member.committee ? member.committee : "")),
      ),
    ];

    const availableBatches = [
      ...new Set(members.map((member) => member.batch)),
    ];

    setAvailableOptions({
      availableDegreePrograms: availableDegrees,
      availableCommittees: availableCommittees,
      availableBatches: availableBatches,
    });
  }, [members]);

  useEffect(() => {
    const currentOptions = JSON.parse(optionsString) as MemberQueryOptions;

    let isCancelled = false;

    const fetchData = async () => {
      setPending(true);
      setError(null);

      const token = getToken();
      if (!token) {
        setMembers(null);
        setPending(false);
        return;
      }

      if (!organizationId) {
        setMembers(null);
        setPending(false);
        return;
      }

      try {
        const responseData = await api.get<Member[]>(
          `/membership/${organizationId}/members`,
          {
            params: currentOptions,
          },
        );

        if (!isCancelled) {
          setMembers(responseData);
        }
      } catch (err) {
        console.error("useMembers: Fetch members failed", err);
        if (!isCancelled) {
          setError(
            err instanceof Error ? err : new Error("Failed to fetch members"),
          );
          setMembers(null);
        }
      } finally {
        if (!isCancelled) {
          setPending(false);
        }
      }
    };

    if (organizationId) {
      fetchData();
    } else {
      setMembers(null);
      setPending(false);
    }

    return () => {
      isCancelled = true;
    };
  }, [optionsString, organizationId]);

  return {
    members,
    stats,
    options: availableOptions,
    pending,
    error,
    setError,
  };
}
