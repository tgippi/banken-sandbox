import {fireEvent, render, screen, waitFor} from '@testing-library/react'
import '@testing-library/jest-dom'
import {OpenAPI} from "./generated_sources";
import nock from "nock";
import {act} from "react";
import BankForm from "./BankForm";

let onSaveCallbackCalled = false;

beforeAll(() => {
    OpenAPI.BASE = 'http://localhost'
});

beforeEach(() => {
    onSaveCallbackCalled = false;
});

test('Anzeige der Bank Form', async () => {
    // ARRANGE
    render(<BankForm />)

    // ACT

    // ASSERT
    expect(screen.getByTestId('bankform-submit')).toBeEnabled()
    expect(screen.getByTestId('bankform-bic')).toBeEnabled()
    expect(screen.getByTestId('bankform-blz')).toBeEnabled()
    expect(screen.getByTestId('bankform-name')).toBeEnabled()
});

test('Bank erstellen erfolgreich', async () => {
    // ARRANGE
    nock(OpenAPI.BASE)
        .put('/api/v1/banken')
        .reply(200);
    render(<BankForm onSave={saveCallback} />)

    // ACT
    await erstelleBank('INGDDEFF', '50010517', 'ING-DIBA');

    // ASSERT
    await waitFor(() => {
        expect(screen.getByTestId('bankform-meldung')).toContainHTML('Bank erfolgreich gespeichert');
        expect(onSaveCallbackCalled).toEqual(true);
    });
});

test('BIC Pflichtfeld', async () => {
    // ARRANGE
    render(<BankForm onSave={saveCallback} />)

    // ACT
    await erstelleBank('', '50010517', 'ING-DIBA');


    // ASSERT
    await waitFor(() => {
        expect(screen.getByTestId('bankform-pflichtfeld-bic')).toContainHTML('* (Pflichtfeld)');
        expect(onSaveCallbackCalled).toEqual(false);
    });
});

test('Name Pflichtfeld', async () => {
    // ARRANGE
    render(<BankForm onSave={saveCallback} />)

    // ACT
    await erstelleBank('INGDDEFF', '50010517', '');

    // ASSERT
    await waitFor(() => {
        expect(screen.getByTestId('bankform-pflichtfeld-name')).toContainHTML('* (Pflichtfeld)');
        expect(onSaveCallbackCalled).toEqual(false);
    });
});

test('BLZ Pflichtfeld', async () => {
    // ARRANGE
    render(<BankForm onSave={saveCallback} />)

    // ACT
    await erstelleBank('INGDDEFF', '', 'ING-DIBA');

    // ASSERT
    await waitFor(() => {
        expect(screen.getByTestId('bankform-pflichtfeld-blz')).toContainHTML('* (Pflichtfeld)');
        expect(onSaveCallbackCalled).toEqual(false);
    });
});

test('Bank erstellen fehlgeschlagen', async () => {
    // ARRANGE
    nock(OpenAPI.BASE)
        .put('/api/v1/banken')
        .reply(400);
    render(<BankForm onSave={saveCallback} />)

    // ACT
    await erstelleBank('INGDDEFF', '50010517', 'ING-DIBA');

    // ASSERT
    await waitFor(() => {
        expect(screen.getByTestId('bankform-meldung')).toContainHTML('Fehler beim Senden der Anfrage');
        expect(onSaveCallbackCalled).toEqual(false);
    });
});

async function erstelleBank(bic: string, blz: string, name: string) {
    await act(async () => {
        fireEvent.input(screen.getByTestId('bankform-bic'), {target: {value: bic}});
        fireEvent.input(screen.getByTestId('bankform-blz'), {target: {value: blz}});
        fireEvent.input(screen.getByTestId('bankform-name'), {target: {value: name}});
    });

    await act(async () => fireEvent.click(screen.getByTestId('bankform-submit')));
}

function saveCallback() {
    onSaveCallbackCalled = true;
}
