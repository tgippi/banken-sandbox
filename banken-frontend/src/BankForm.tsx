import {Bank, BankService} from "./generated_sources";
import {FieldValues, useForm} from "react-hook-form";
import {useState} from "react";

function BankForm(props: any) {

    const {register, handleSubmit, reset, formState: {errors}} = useForm();

    const {erstelleBank} = BankService;

    const [meldung, setMeldung] = useState<string>("");

    function submitBank(bankDaten: FieldValues) {
        erstelleBank({
            name: bankDaten["name"],
            blz: bankDaten["blz"],
            bic: bankDaten["bic"]
        } as Bank).then(
            () => {
                setMeldung("Bank erfolgreich gespeichert");
                reset();
                props.onSave();
            }, (e) => {
                setMeldung("Fehler beim Senden der Anfrage");
            }
        );
    }

    function ValidierungsFehler() {
        return <span className="validierungsFehler">* (Pflichtfeld)</span>
    }

    return (
        <>
            <form onSubmit={handleSubmit((data) => submitBank(data))}>
                {meldung && <span>{meldung}</span>}

                <p>
                    <input {...register("name", {required: true})} id="name" name="name" type="text"
                           placeholder="Name"/>
                    {errors.name && <ValidierungsFehler></ValidierungsFehler>}
                </p>

                <p>
                    <input {...register("blz", {required: true})} id="blz" name="blz" type="text" placeholder="BLZ"/>
                    {errors.blz && <ValidierungsFehler></ValidierungsFehler>}
                </p>

                <p>
                    <input {...register("bic", {required: true})} id="bic" name="bic" type="text" placeholder="BIC"/>
                    {errors.bic && <ValidierungsFehler></ValidierungsFehler>}
                </p>

                <input type="submit" value="Bank speichern"/>
            </form>
        </>
    )
}

export default BankForm;
