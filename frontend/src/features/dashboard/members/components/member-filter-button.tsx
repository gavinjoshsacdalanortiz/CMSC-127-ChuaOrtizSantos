import { ChangeEventHandler } from "react";

type Props = {
  name: string;
  label: string;
  activated: boolean;
  options: string[];
  onChange: ChangeEventHandler<HTMLSelectElement>;
};
const MemberFilterButton = (props: Props) => {
  return (
    <select
      name={props.name}
      className={`${props.activated ? "bg-neutral text-neutral-content" : "!btn-outline"}  select  w-fit rounded-xl border-neutral  select-sm transition-all`}
      defaultValue={props.label}
      onChange={props.onChange}
    >
      <option disabled>{props.label}</option>
      {props.options.map((option) => (
        <option key={option}>{option}</option>
      ))}
    </select>
  );
};

export default MemberFilterButton;
