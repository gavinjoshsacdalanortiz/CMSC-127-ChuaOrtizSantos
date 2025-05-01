import MemberFilterButton from "@/features/dashboard/components/member-filter-button";
import { getDayName, getMonthName } from "@/utils/date";

const MembersDashboard = () => {
  const today = new Date();

  return (
    <>
      <div className="flex justify-between mb-12">
        <div className="space-y-1">
          <h2 className="font-semibold">Members</h2>
          <div className="text-xs text-base-content/60">
            <span className="text-secondary font-semibold">
              {getDayName(today.getDay())}
            </span>
            , {today.getDate()} {getMonthName(today.getMonth())}{" "}
            {today.getFullYear()}
          </div>
        </div>

        <div className="space-y-1">
          <select className="select border-none rounded-box bg-base-100">
            <option>2023</option>
            <option>2022</option>
          </select>
        </div>
      </div>

      <div className="flex gap-2">
        <MemberFilterButton label="Role" activated={false} />
        <MemberFilterButton label="Status" activated={false} />
        <MemberFilterButton label="Gender" activated={false} />
        <MemberFilterButton label="Degree Program" activated={false} />
        <MemberFilterButton label="Committee" activated={false} />
      </div>
    </>
  );
};

export default MembersDashboard;
