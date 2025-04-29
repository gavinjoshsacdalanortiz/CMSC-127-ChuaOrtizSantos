const createEnv = () => {
  const envVars = Object.entries(import.meta.env).reduce<
    Record<string, string | undefined>
  >((acc, curr) => {
    const [key, value] = curr;
    if (key.startsWith("VITE_APP_")) {
      acc[key.replace("VITE_APP_", "")] = value;
    }
    return acc;
  }, {});

  const validatedEnv: { API_URL: string } = {
    API_URL: "http://localhost:3000",
  };

  if (envVars.API_URL) {
    validatedEnv.API_URL = envVars.API_URL;
  }

  if (typeof validatedEnv.API_URL !== "string") {
    throw new Error(`Invalid env provided.
The following variables are missing or invalid:
- API_URL: Expected a string, but got ${typeof validatedEnv.API_URL}
`);
  }

  return validatedEnv;
};

export const env = createEnv();
