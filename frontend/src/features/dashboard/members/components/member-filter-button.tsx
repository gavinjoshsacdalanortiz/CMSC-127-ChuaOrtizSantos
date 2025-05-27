import { toTitleCase } from "@/lib/utils";
import { ChangeEventHandler } from "react";
import { GrClose } from "react-icons/gr";

type Props = {
  name: string;
  label: string;
  activated: boolean;
  options: string[];
  onChange: ChangeEventHandler<HTMLSelectElement>;
  onClear?: (name: string) => void;
};
const MemberFilterButton = (props: Props) => {
  return (
    <div className="flex gap-1 place-items-center">
      <select
        name={props.name}
        className={`${props.activated ? "bg-accent text-neutral-content" : "!btn-outline border-neutral/10"}  select  w-fit rounded-xl border-neutral  select-sm transition-all`}
        defaultValue={props.label}
        onChange={props.onChange}
      >
        <option disabled>{props.label}</option>
        {props.options.map((option) => (
          <option key={option} value={option}>
            {toTitleCase(option)}
          </option>
        ))}
      </select>
      {props.activated && props.onClear != undefined && (
        <button
          className="btn btn-circle btn-xs border-none"
          onClick={() => props.onClear!(props.name)}
          title={`Clear ${props.label} filter`}
        >
          <GrClose />
        </button>
      )}
    </div>
  );
};

export default MemberFilterButton;
