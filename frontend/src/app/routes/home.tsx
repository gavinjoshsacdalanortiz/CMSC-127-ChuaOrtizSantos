import { paths } from "@/config/paths";
import LoginForm from "@/features/auth/components/login-form";
import { useAuth } from "@/lib/auth";
import { useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router";

const Home = () => {
  const { member } = useAuth();

  const [searchParams] = useSearchParams();
  const redirectTo = searchParams.get("redirectTo");

  const navigate = useNavigate();

  useEffect(() => {
    if (member) {
      navigate(redirectTo ? redirectTo : paths.app.dashboard.root.path, {
        replace: true,
      });
    }
  }, [member, navigate, redirectTo]);

  return (
    <main className="overflow-hidden">
      <div className="bg-[url(../../../home_bg.jpg)] bg-cover h-screen pt-12">
        <section className="bg-neutral/30 m-auto w-[90vw] h-[90dvh] rounded-box backdrop-blur-xs">
          <div className="grid xl:grid-cols-2 gap-8 xl:gap-0 h-full place-content-center md:items-center">
            <div className="md:mt-0 text-center text-neutral-content">
              <h1 className="text-5xl md:text-6xl xl:text-8xl font-display font-black h-fit">
                Welcome
              </h1>
              <p>Sign in to access your profile.</p>
            </div>
            <LoginForm />
          </div>
        </section>
      </div>
    </main>
  );
};
export default Home;
