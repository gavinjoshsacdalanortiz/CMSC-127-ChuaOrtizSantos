import { createPortal } from "react-dom";

type Props = {
  show: boolean;
  message: string;
  type: "success" | "error";
};

const Toast = (props: Props) => {
  return createPortal(
    <>
      {props.show ? (
        <div className="toast toast-end">
          <div
            className={`alert ${props.type === "success" ? "alert-success" : "alert-error"}`}
          >
            <span>{props.message}</span>
          </div>
        </div>
      ) : null}
    </>,
    document.getElementById("teleports") as Element,
  );
};

export default Toast;
