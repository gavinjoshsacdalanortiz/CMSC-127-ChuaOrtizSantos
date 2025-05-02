import { useAuth } from "@/lib/auth";
import { CgLock } from "react-icons/cg";
import { FiMail } from "react-icons/fi";

const LoginForm = () => {
  const { login, loading } = useAuth();

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const form = e.target as HTMLFormElement & {
      email: { value: string };
      password: { value: string };
    };

    const email = form.email.value.trim();
    const password = form.password.value;

    login({ username: email, password });
  };

  return (
    <form
      className="w-2/3 mx-auto  p-4 text-neutral-content bg-base-200/10 backdrop-blur-sm rounded-box"
      onSubmit={handleSubmit}
    >
      <h2 className="font-display hidden mb-4 text-3xl xl:block">Login</h2>
      <label className="validator input mb-2 has-user-invalid:mb-0 w-full bg-transparent peer">
        <FiMail />
        <input name="email" type="email" placeholder="Email address" required />
      </label>
      <div className="validator-hint peer-has-user-invalid:!inline peer-user-invalid:inline mb-2 hidden">
        Enter a valid email address.
      </div>

      <label className="validator input w-full bg-transparent peer">
        <CgLock />
        <input
          name="password"
          type="password"
          placeholder="Password"
          minLength={8}
          required
        />
      </label>
      <div className="validator-hint peer-has-user-invalid:!inline peer-user-invalid:inline mb-2 hidden">
        Password length must be at least 8
      </div>

      <button
        type="submit"
        className="btn btn-primary mt-4 btn-block join-item"
      >
        Sign In
      </button>
    </form>
  );
};

export default LoginForm;
