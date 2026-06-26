import type { PlayerStateDto } from "../types/requestTypes";

interface Props {
    player: PlayerStateDto;
    label: string;
    onChange: (updated: PlayerStateDto) => void;
}

const fields: { key: keyof PlayerStateDto; label: string }[] = [
  { key: 'terraformRating',     label: 'TR' },
  { key: 'megacredits',         label: 'MC' },
  { key: 'mcProduction',        label: 'MC Prod' },
  { key: 'steel',               label: 'Steel' },
  { key: 'steelProduction',     label: 'Steel Prod' },
  { key: 'titanium',            label: 'Titanium' },
  { key: 'titaniumProduction',  label: 'Ti Prod' },
  { key: 'energy',              label: 'Energy' },
  { key: 'energyProduction',    label: 'Energy Prod' },
  { key: 'heat',                label: 'Heat' },
  { key: 'heatProduction',      label: 'Heat Prod' },
  { key: 'plants',              label: 'Plants' },
  { key: 'plantProduction',     label: 'Plant Prod' },
  { key: 'cityCount',           label: 'Cities' },
  { key: 'greeneryCount',       label: 'Greeneries' },
  { key: 'scienceTagCount',     label: 'Science Tags' },
  { key: 'buildingTagCount',    label: 'Building Tags' },
];

export function PlayerSection({ player, label, onChange }: Props) {
  const set = (key: keyof PlayerStateDto, value: number) =>
    onChange({ ...player, [key]: value });

    return (
        <section>
            <h3>{label}</h3>
            <div className="grid-3">
                {fields.map(f => (
                <label key={f.key}>
                    {f.label}
                    <input
                    type="number"
                    value={player[f.key] as number}
                    onChange={e => set(f.key, +e.target.value)}
                    />
                </label>
                ))}
            </div>
        </section>
    );
}