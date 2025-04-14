import { useEffect } from "react";

const App = () => {
  useEffect(() => {
    (async () => {
      const response = await fetch("http://localhost:8080/users");
      const data = await response.json();

      console.log(data);
    })();
  }, []);

  return <h1 className="text-amber-300">Hello</h1>;
};

export default App;
