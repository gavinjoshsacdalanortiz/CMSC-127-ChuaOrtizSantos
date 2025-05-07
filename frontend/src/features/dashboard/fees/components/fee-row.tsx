import { Fee } from "@/types/fee";

type Props = { organizationName: string; isPaid: boolean } & Pick<
  Fee,
  "dueDate" | "amount"
>;
const FeeRow = (props: Props) => {
  return (
    <tr>
      <th>
        <label>
          <input type="checkbox" className="checkbox" />
        </label>
      </th>
      <td>
        <div className="flex items-center gap-3">
          <div>
            <div className="font-bold">{props.organizationName}</div>
          </div>
        </div>
      </td>
      <td>
        <div>{props.dueDate}</div>
      </td>
      <td>
        <div className="font-semibold">{props.amount}</div>
      </td>
      <td>
        {isPaid ? (
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
