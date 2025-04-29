import { FaShoppingBag } from "react-icons/fa";

const AppHeader = () => {
  return (
    <div className="navbar absolute bg-transparent ">
      <div className="flex-1">
        <button className="btn btn-square btn-ghost pt-2 text-neutral-content hover:bg-transparent flex flex-col aspect-square size-16">
          <FaShoppingBag className="size-6" />
          <div>Shop</div>
        </button>
      </div>
      <div className="flex-none">
        <button className="btn btn-square btn-ghost"></button>
      </div>
    </div>
  );
};

export default AppHeader;
