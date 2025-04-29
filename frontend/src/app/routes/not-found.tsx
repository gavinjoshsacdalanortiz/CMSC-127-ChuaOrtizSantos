import { paths } from "@/config/paths";

const NotFoundRoute = () => {
  return (
    <div className="min-h-screen flex flex-col items-center justify-center">
      <h1 className="text-3xl mb-4">404 - Not Found</h1>
      <p>Sorry, the page you are looking for does not exist.</p>
      <a className="link link-neutral" href={paths.home.getHref()}>
        Go to Login
      </a>
    </div>
  );
};

export default NotFoundRoute;
