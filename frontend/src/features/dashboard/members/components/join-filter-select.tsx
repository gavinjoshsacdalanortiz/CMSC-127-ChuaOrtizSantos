import { ChangeEventHandler } from "react";

type JoinedFilterSelectProps = {
  name: string; // To identify the filter in handlers
  label: string; // Placeholder text
  value: string | undefined; // Current selected value, undefined for placeholder
  options: string[]; // Array of option values (and display text if not transforming)
  onChange: ChangeEventHandler<HTMLSelectElement>;
  onClear: (name: string) => void; // Handler to clear this specific filter
  disabled?: boolean; // Optional: to disable the select
  isLoading?: boolean; // Optional: to show a loading state for options
};

const JoinedFilterSelect = (props: JoinedFilterSelectProps) => {
  const {
    name,
    label,
    value,
    options,
    onChange,
    onClear,
    disabled = false,
    isLoading = false,
  } = props;

  const isActivated = value !== undefined && value !== label;

  const toTitleCase = (str: string) => {
    if (!str) return "";
    return str.replace(
      /\w\S*/g,
      (text) => text.charAt(0).toUpperCase() + text.substring(1).toLowerCase(),
    );
  };

  return (
    <div className="relative inline-flex items-center join-item">
      <select
        name={name}
        onChange={onChange}
        defaultValue={props.label}
        disabled={disabled || isLoading}
        className={`
          select select-sm rounded-none 
          ${isActivated ? "bg-accent text-neutral-content" : "!btn-outline border-neutral/10"}
          ${isActivated ? "pr-7" : ""} // Padding for clear button
          transition-all 
          focus:z-10 // Ensure focused select is on top in a join
        `}
      >
        <option disabled>{props.label}</option>
        {options.map((option) => (
          <option key={option}>{toTitleCase(option)} </option>
        ))}
      </select>
      {isActivated && (
        <button
          type="button"
          onClick={() => onClear(name)}
          className={`
            absolute right-1 top-1/2 -translate-y-1/2 transform
            btn btn-ghost btn-xs btn-circle p-0 m-0
            flex items-center justify-center
            text-neutral-content/70 hover:text-neutral-content
            focus:outline-none
          `}
          style={{ width: "1.25rem", height: "1.25rem" }}
          aria-label={`Clear ${label} filter`}
          title={`Clear ${label} filter`}
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="h-3.5 w-3.5"
            viewBox="0 0 20 20"
            fill="currentColor"
          >
            <path
              fillRule="evenodd"
              d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
              clipRule="evenodd"
            />
          </svg>
        </button>
      )}
    </div>
  );
};

export default JoinedFilterSelect;
