import {IbanRequest, IbanResponse, IbanService} from "./generated_sources";
import {FieldValues, useForm} from "react-hook-form";
import {useState} from "react";

function IbanForm() {
    const {register, handleSubmit, formState: {errors}} = useForm();

    const {pruefeIban} = IbanService;

    const [ibanInvalid, setIbanInvalid] = useState<boolean>(false);
    const [bankName, setBankName] = useState<string>();
    const [bic, setBIC] = useState<string>();
    const [blz, setBLZ] = useState<string>();
    const [meldung, setMeldung] = useState<string>("");

    function submitIban(ibanDaten: FieldValues) {
        pruefeIban({
            iban: ibanDaten["iban"]
        } as IbanRequest).then(
            (res: IbanResponse) => {
                setMeldung("");
                setIbanInvalid(!res.isValid);
                setBankName(res.bank?.name || "");
                setBIC(res.bank?.bic || "");
                setBLZ(res.bank?.blz || "");
            }, () => {
                setMeldung("Fehler beim Senden der Anfrage");
            }
        )
    }


    function ValidierungsFehler() {
        return <span className="validierungsFehler">* (Pflichtfeld)</span>
    }

    function IbanFehler() {
        return <span className="ibanUngueltig" data-testid="iban-ungueltig">* (Iban ungültig)</span>
    }

    return (
        <>
            <h1>IBAN</h1>
            {meldung && <span>{meldung}</span>}
            <form onSubmit={handleSubmit((data) => submitIban(data))}>
                <p>
                    <input {...register("iban", {required: true})} id="iban" name="iban" type="text"
                           data-testid="iban" placeholder="iban"/>
                    {errors.iban && <ValidierungsFehler></ValidierungsFehler>}
                    {(!errors.iban && ibanInvalid) && <IbanFehler></IbanFehler>}
                </p>

                <p>
                    <input id="bankname" name="bankname" type="text" readOnly={true} value={bankName}
                           data-testid="bankname" placeholder="Bank"/>
                </p>
                <p>
                    <input id="blz" name="blz" type="text" readOnly={true} value={blz} placeholder="BLZ"
                           data-testid="blz"/>
                </p>

                <p>
                    <input id="bic" name="bic" type="text" readOnly={true} value={bic} placeholder="BIC"
                           data-testid="bic"/>
                </p>

                <input type="submit" value="IBAN Prüfen" data-testid="iban-submit"/>
            </form>
        </>
    )
}

export default IbanForm;
