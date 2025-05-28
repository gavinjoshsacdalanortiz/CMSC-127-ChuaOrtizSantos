import { Fee } from "@/types/fee";

type Props = {
  memberName?: string;
  semesterIssued: string;
  isPaid: boolean;
  index: number;
  datePaid?: string;
} & Pick<Fee, "dueDate" | "amount">;
const FeeRow = (props: Props) => {
  return (
    <tr>
      <td className="text-2xl font-thin opacity-50 tabular-nums">
        {props.index}
      </td>
      {props.memberName && (
        <td>
          <div>{props.memberName}</div>
        </td>
      )}
      <td>
        <div>{props.semesterIssued}</div>
      </td>
      <td>
        <div>{props.dueDate}</div>
      </td>
      <td>
        <div className="font-semibold">₱{props.amount}</div>
      </td>
      <td>
        {props.isPaid ? (
          <span className="badge badge-success badge-xs md:bagde-xs ">
            Paid
          </span>
        ) : (
          <span className="badge badge-error badge-xs md:bagde-xs ">
            Not paid
          </span>
        )}
      </td>
      {props.datePaid && (
        <td>
          <div className="font-semibold">₱{props.datePaid}</div>
        </td>
      )}
    </tr>
  );
};

export default FeeRow;
