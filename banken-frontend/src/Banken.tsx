import BankenUebersicht from "./BankenUebersicht";
import BankForm from "./BankForm";
import {useState} from "react";

function Banken() {

    const [updates, setUpdates] = useState<number>(0);

    function triggerUpdateState() {
        setUpdates(updates + 1);
    }

    return (
      <>
          <h1>Banken</h1>

          <BankForm onSave={triggerUpdateState}></BankForm>

          <BankenUebersicht key={updates}></BankenUebersicht>
      </>
    );
}

export default Banken;
