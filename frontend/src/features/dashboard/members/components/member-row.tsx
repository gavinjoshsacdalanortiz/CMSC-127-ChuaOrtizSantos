import { Member } from "@/types/member";

type Props = { member: Member; index: number };
const MemberRow = (props: Props) => {
  return (
    <tr>
      <td className="text-2xl font-thin opacity-50 tabular-nums">
        {props.index}
      </td>
      <td>
        <div className="">
          {props.member.firstName + " " + props.member.lastName}
        </div>
      </td>
      <td>
        <div>{props.member.degreeProgram}</div>
      </td>
      <td>
        <div className="">{props.member.batch}</div>
      </td>
      <td>
        <div className="">{props.member.email}</div>
      </td>
      <td>
        <div className="">{props.member.gender}</div>
      </td>
      <td>
        <div className="">{props.member.committee}</div>
      </td>
      <td>
        <div className="">{props.member.status}</div>
      </td>
    </tr>
  );
};

export default MemberRow;
