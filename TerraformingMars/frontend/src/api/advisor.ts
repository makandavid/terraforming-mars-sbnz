import type { GameStateRequest } from "../types/requestTypes";
import type { AnalysisResponse, CardCatalogEntry } from "../types/responseTypes";

const BASE_URL = 'http://localhost:8080/api';

export async function analyzeGame(request: GameStateRequest): Promise<AnalysisResponse> {
    const response = await fetch(`${BASE_URL}/analyze`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(request),
    });
    if (!response.ok) {
        throw new Error(`Analysis failed: ${response.statusText}`);
    }
    return response.json();
}

export async function fetchCards(): Promise<CardCatalogEntry[]> {
    const response = await fetch(`${BASE_URL}/cards`);
    if (!response.ok) {
        throw new Error('Failed to fetch card catalog');
    }
    return response.json();
}

