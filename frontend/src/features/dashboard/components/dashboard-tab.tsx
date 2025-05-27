type Props = {
  leadingIcon: React.ReactNode;
  label: string;
  isSelected: boolean;
  onClick: () => void;
};

const DashboardTab = (props: Props) => {
  return (
    <button
      title={props.label}
      role="radio"
      onClick={props.onClick}
      className={`flex hover:bg-base-200 place-items-center gap2 text-sm rounded-box btn-full  xl:justify-start group p-2 transition-colors w-full cursor-pointer ${props.isSelected ? "!bg-primary" : "bg-base-100"}`}
    >
      <span className="ml-2 not-xl:mx-auto">{props.leadingIcon}</span>
      <span className="ml-0 xl:ml-3 hidden xl:inline-flex whitespace-nowrap">
        {props.label}
      </span>
    </button>
  );
};

export default DashboardTab;
