import type { GameStateRequest } from "../types/requestTypes";

interface Props {
    state: GameStateRequest;
    onChange: (updated: GameStateRequest) => void;
}

export function GlobalStateSection({state, onChange}: Props) {
    const set = (field: keyof GameStateRequest, value: number) =>
        onChange({...state, [field]: value});

    return (
        <section>
            <h3>Global State</h3>
            <div className="grid-2">
                <label>
                Generation
                <input type="number" min={1} max={14} value={state.generation}
                    onChange={e => set('generation', +e.target.value)} />
                </label>
                <label>
                Oxygen (%)
                <input type="number" min={0} max={14} step={1} value={state.oxygenLevel}
                    onChange={e => set('oxygenLevel', +e.target.value)} />
                </label>
                <label>
                Temperature (°C)
                <input type="number" min={-30} max={8} step={2} value={state.temperature}
                    onChange={e => set('temperature', +e.target.value)} />
                </label>
                <label>
                Oceans
                <input type="number" min={0} max={9} value={state.oceanCount}
                    onChange={e => set('oceanCount', +e.target.value)} />
                </label>
                <label>
                Last Temp Raised (gen)
                <input type="number" min={0} value={state.lastTempRaisedGeneration}
                    onChange={e => set('lastTempRaisedGeneration', +e.target.value)} />
                </label>
            </div>
        </section>
    );
}