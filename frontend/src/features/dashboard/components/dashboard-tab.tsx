import { Link } from "react-router";

type Props = {
  leadingIcon: React.ReactNode;
  label: string;
  isSelected: boolean;
  updateTab: (label: string) => void;
};

const DashboardTab = (props: Props) => {
  return (
    <Link
      to={props.label.toLowerCase()}
      role="radio"
      onClick={() => props.updateTab(props.label)}
      className={`flex hover:bg-base-200 place-items-center gap-2 text-sm rounded-box p-4 py-2 transition-colors w-full cursor-pointer ${props.isSelected ? "!bg-primary" : "bg-base-100"}`}
    >
      {props.leadingIcon}
      <div>{props.label}</div>
    </Link>
  );
};

export default DashboardTab;
