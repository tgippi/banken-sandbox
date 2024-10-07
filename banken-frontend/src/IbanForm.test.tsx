import {fireEvent, render, screen, waitFor} from '@testing-library/react'
import '@testing-library/jest-dom'
import IbanForm from "./IbanForm";
import {IbanResponse, OpenAPI} from "./generated_sources";
import nock from "nock";
import {act} from "react";
import {getValue} from "@testing-library/user-event/dist/utils";

const iban = 'DE02120300000000202051';

beforeAll(() => {
    OpenAPI.BASE = 'http://localhost'
});

test('Anzeige der Iban Form', async () => {
    // ARRANGE
    render(<IbanForm />)

    // ACT
    await screen.findByRole('heading')

    // ASSERT
    expect(screen.getByRole('heading')).toHaveTextContent('IBAN')
    expect(screen.getByTestId('iban-submit')).toBeEnabled()
    expect(screen.getByTestId('iban')).toBeEnabled()
    expect(screen.getByTestId('bankname')).toBeEnabled()
    expect(screen.getByTestId('blz')).toBeEnabled()
    expect(screen.getByTestId('bic')).toBeEnabled()
});

test('Gültige IBAN validieren', async () => {
    nock(OpenAPI.BASE)
        .post('/api/v1/iban')
        .reply(200, {
            isValid: true
        } as IbanResponse);

    // ARRANGE
    render(<IbanForm/>)

    // ACT
    await screen.findByRole('heading')
    await pruefeIban(iban);

    // ASSERT
    expect(screen.queryByTestId('iban-ungueltig')).toBeNull();
});


test('Ungültige IBAN validieren', async () => {
    const scope = nock(OpenAPI.BASE)
        .post('/api/v1/iban')
        .reply(200, {
            isValid: false
        } as IbanResponse);

    // ARRANGE
    render(<IbanForm/>)

    // ACT
    await screen.findByRole('heading')
    await pruefeIban(iban);

    // ASSERT
    await waitFor(() => {
        expect(screen.getByTestId('iban-ungueltig')).toBeVisible();
    });
});

test('Bank abfragen', async () => {
    nock(OpenAPI.BASE)
        .post('/api/v1/iban')
        .reply(200, {
            isValid: true,
            bank: {
                name: 'DEUTSCHE KREDITBANK BERLIN',
                bic: 'BYLADEM1001',
                blz: '12030000'
            }
        } as IbanResponse);

    // ARRANGE
    render(<IbanForm/>)

    // ACT
    await screen.findByRole('heading')
    await pruefeIban(iban);

    // ASSERT
    waitFor(() => {
        expect(getValue(screen.getByTestId('bankname'))).toEqual('DEUTSCHE KREDITBANK BERLIN');
        expect(getValue(screen.getByTestId('blz'))).toEqual('BYLADEM1001');
        expect(getValue(screen.getByTestId('bic'))).toEqual('12030000');
    });
});


async function pruefeIban(iban: string) {
    await act(async () => fireEvent.input(screen.getByTestId('iban'), {
        target: {value: iban}
    }));

    await act(async () => fireEvent.click(screen.getByTestId('iban-submit')));
}
