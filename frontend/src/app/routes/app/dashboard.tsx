import { BsCash } from "react-icons/bs";
import { FiUser } from "react-icons/fi";

const Dashboard = () => {
  return (
    <main className="grid grid-cols-[0.75fr_3fr_0.25fr] bg-base-100 *:size-full h-screen">
      <section className="p-4 w-full space-y-2 [&_button]:btn-full">
        <select className="select bg-base-200 mb-8">
          <option>test</option>
          <option>test1</option>
        </select>

        <div className="flex hover:bg-base-200 place-items-center gap-2 text-sm rounded-md font-semibold p-2">
          <FiUser />
          <div>Members</div>
        </div>

        <div className="flex hover:bg-base-200 place-items-center gap-2 text-sm rounded-md font-semibold p-2">
          <BsCash />
          <div>Fees</div>
        </div>
      </section>
      <section className="bg-base-200"></section>
      <section></section>
    </main>
  );
};

export default Dashboard;
