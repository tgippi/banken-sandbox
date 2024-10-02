import {Bank, BankService} from "./generated_sources";
import {useEffect, useState} from "react";

function BankenUebersicht() {

    const [banken, setBanken] = useState<Array<Bank>>([])

    const {alleBanken} = BankService;
    useEffect(() => {
        alleBanken().then(
            (bankenRes: Array<Bank>) => setBanken(bankenRes),
            () => setBanken([])
        );
    }, []);

    return (
        <>
            <table>
                <thead>
                <tr>
                    <th>BLZ</th>
                    <th>Name</th>
                    <th>BIC</th>
                </tr>
                </thead>
                <tbody>
                {banken.map((bank: Bank) => (
                    <BankTabellenZeile blz={bank.blz} name={bank.name} bic={bank.bic}></BankTabellenZeile>
                ))}
                </tbody>
            </table>
        </>
    );
}

function BankTabellenZeile(bank: Bank) {
    return (
        <>
            <tr>
                <td>{bank.blz}</td>
                <td>{bank.name}</td>
                <td>{bank.bic}</td>
            </tr>
        </>
    )
}

export default BankenUebersicht;
