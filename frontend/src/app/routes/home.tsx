import LoginForm from "@/features/auth/components/login-form";
import RegisterForm from "@/features/auth/components/register-form";
import { useState } from "react";

const Home = () => {
  const [isSignIn, setIsSignIn] = useState(true);

  const toggleIsSignIn = () => {
    setIsSignIn((isSignIn) => !isSignIn);
  };

  return (
    <main className="overflow-hidden">
      <div className="bg-[url(../../../public/home_bg.jpg)] bg-cover h-screen pt-12">
        <section className="bg-neutral/30 m-auto w-[90vw] h-[90dvh] rounded-box backdrop-blur-xs">
          <div className="grid xl:grid-cols-2 gap-8 xl:gap-0 h-full place-content-center md:items-center">
            <div className="md:mt-0 text-center space-y-2">
              <h1 className="text-5xl md:text-6xl xl:text-8xl font-display  text-neutral-content font-black h-fit">
                Welcome
              </h1>
              <div className="join text-neutral-content text-xs md:text-sm xl:text-lg">
                <input
                  className="hover:not-checked:bg-transparent join-item btn btn-sm btn-accent btn-outline"
                  onClick={toggleIsSignIn}
                  type="radio"
                  name="options"
                  aria-label="Sign in"
                  defaultChecked={isSignIn}
                />
                <input
                  className="hover:not-checked:bg-transparent join-item btn btn-outline  btn-accent btn-sm"
                  onClick={toggleIsSignIn}
                  type="radio"
                  name="options"
                  aria-label="Sign up"
                  defaultChecked={!isSignIn}
                />
              </div>
            </div>
            {isSignIn ? <LoginForm /> : <RegisterForm />}
          </div>
        </section>
      </div>
    </main>
  );
};
export default Home;
