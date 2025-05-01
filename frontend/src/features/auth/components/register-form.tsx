import { useAuth } from "@/lib/auth";
import { CgLock } from "react-icons/cg";
import { FiMail, FiUser } from "react-icons/fi";

const RegisterForm = () => {
  const { register } = useAuth();

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const form = e.target as HTMLFormElement & {
      firstName: { value: string };
      lastName: { value: string };
      email: { value: string };
      password: { value: string };
    };

    const firstName = form.firstName.value.trim();
    const lastName = form.lastName.value.trim();
    const email = form.email.value.trim();
    const password = form.password.value;

    register({ firstName, lastName, email, password });
  };

  return (
    <form
      className="w-2/3 mx-auto p-4 text-neutral-content bg-base-200/10 backdrop-blur-sm rounded-box"
      onSubmit={handleSubmit}
    >
      <h2 className="font-display hidden mb-4 text-3xl xl:block">Register</h2>

      <label className="validator peer-has-user-invalid:!inline peer-user-invalid:inline mb-2 input w-full bg-transparent">
        <FiUser />
        <input name="firstName" type="text" placeholder="First Name" required />
      </label>

      <label className="validator peer-has-user-invalid:!inline peer-user-invalid:inline mb-2 input w-full bg-transparent">
        <FiUser />
        <input name="lastName" type="text" placeholder="Last Name" required />
      </label>

      <label className="validator input mb-2 has-user-invalid:mb-0 w-full bg-transparent peer">
        <FiMail />
        <input
          v-model="email"
          type="email"
          placeholder="Email address"
          required
        />
      </label>
      <div className="validator-hint peer-has-user-invalid:!inline peer-user-invalid:inline mb-2 hidden">
        Enter a valid email address.
      </div>

      <label className="validator input w-full has-user-invalid:mb-0 mb-2 bg-transparent peer">
        <CgLock />
        <input
          v-model="password"
          type="password"
          placeholder="Password"
          minLength={8}
          required
        />
      </label>
      <div className="validator-hint peer-has-user-invalid:!inline peer-user-invalid:inline mb-2 hidden">
        Password length must be at least 8
      </div>

      <button className="btn btn-primary mt-4 btn-block join-item">
        Create Account
      </button>
    </form>
  );
};

export default RegisterForm;
