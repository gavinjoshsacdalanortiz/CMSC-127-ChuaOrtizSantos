import { getDayName, getMonthName } from "@/utils/date";

type Props = {
  title: string;
};

const DashboardTitle = (props: Props) => {
  const today = new Date();
  return (
    <div className="space-y-1">
      <h2 className="font-semibold">{props.title}</h2>
      <div className="text-xs text-base-content/60">
        <span className="text-secondary font-semibold">
          {getDayName(today.getDay())}
        </span>
        , {today.getDate()} {getMonthName(today.getMonth())}{" "}
        {today.getFullYear()}
      </div>
    </div>
  );
};

export default DashboardTitle;
