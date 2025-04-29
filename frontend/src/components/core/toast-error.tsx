type Props = {
  show: boolean;
  message: string;
};

const ToastError = (props: Props) => {
  return (
    <>
      {props.show ? (
        <div className="toast toast-top toast-center">
          <div className="alert alert-error">
            <span>{props.message}</span>
          </div>
        </div>
      ) : null}
    </>
  );
};

export default ToastError;
