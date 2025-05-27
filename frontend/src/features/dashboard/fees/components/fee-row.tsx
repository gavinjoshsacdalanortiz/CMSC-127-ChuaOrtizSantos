import { Fee } from "@/types/fee";

type Props = { semesterIssued: string; isPaid: boolean; index: number } & Pick<
  Fee,
  "dueDate" | "amount"
>;
const FeeRow = (props: Props) => {
  return (
    <tr>
      <td className="text-2xl font-thin opacity-50 tabular-nums">
        {props.index}
      </td>
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
    </tr>
  );
};

export default FeeRow;
