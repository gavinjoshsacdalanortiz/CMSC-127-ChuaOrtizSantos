type Props = {
  label: string;
  activated: boolean;
  options: string[];
  onChange: (value: string) => void;
};
const MemberFilterButton = (props: Props) => {
  return (
    <select
      className={`${props.activated ? "bg-neutral text-neutral-content" : "!btn-outline"}  select  w-fit rounded-xl border-neutral  select-sm transition-all`}
      defaultValue={props.label}
      onChange={(event) => props.onChange(event.target.value)}
    >
      <option disabled>{props.label}</option>
      {props.options.map((option) => (
        <option key={option}>{option}</option>
      ))}
    </select>
  );
};

export default MemberFilterButton;
