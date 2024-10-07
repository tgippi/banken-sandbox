import {render, screen, waitFor} from '@testing-library/react'
import '@testing-library/jest-dom'
import {Bank, OpenAPI} from "./generated_sources";
import nock from "nock";
import BankenUebersicht from "./BankenUebersicht";

beforeAll(() => {
    OpenAPI.BASE = 'http://localhost'
});

test('Anzeige der Banken', async () => {
    // ARRANGE
    nock(OpenAPI.BASE)
        .get('/api/v1/banken')
        .reply(200, [
            {
                name: 'DEUTSCHE KREDITBANK BERLIN',
                blz: '12030000',
                bic: 'BYLADEM1001'
            }
        ] as Bank[]);
    render(<BankenUebersicht />)

    // ACT

    // ASSERT
    await waitFor(() => {
        expect(screen.getByRole('table')).toBeVisible()
        expect(screen.getByText('DEUTSCHE KREDITBANK BERLIN')).toBeVisible()
        expect(screen.getByText('12030000')).toBeVisible()
        expect(screen.getByText('BYLADEM1001')).toBeVisible()
    });
});


test('Laden der Banken fehlgeschlagen', async () => {
    // ARRANGE
    nock(OpenAPI.BASE)
        .get('/api/v1/banken')
        .reply(500);
    render(<BankenUebersicht />)

    // ACT

    // ASSERT
    await waitFor(() => {
        expect(screen.getByRole('table')).toBeVisible()
    });
});
