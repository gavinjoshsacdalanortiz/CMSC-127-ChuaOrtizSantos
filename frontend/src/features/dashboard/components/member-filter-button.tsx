type Props = {
  label: string;
  activated: boolean;
};
const MemberFilterButton = (props: Props) => {
  return (
    <button
      className={`btn rounded-xl border-neutral-400 btn-sm font-semibold btn-neutral transition-all ${props.activated ? "border-none" : "!btn-outline"}`}
    >
      <div>{props.label}</div>
    </button>
  );
};

export default MemberFilterButton;
