import { MapContainer, Marker, Popup, TileLayer, Circle } from 'react-leaflet';
import L from 'leaflet';

const icon = new L.Icon({
  iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
  shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
  iconSize: [25, 41],
  iconAnchor: [12, 41]
});

export default function ComplaintMap({ complaints = [], hotspots = [] }) {
  const center = complaints[0] ? [complaints[0].latitude, complaints[0].longitude] : [20.5937, 78.9629];
  return (
    <div className="h-[520px] overflow-hidden rounded-lg border border-slate-200 bg-white shadow-soft">
      <MapContainer center={center} zoom={complaints[0] ? 11 : 5} scrollWheelZoom>
        <TileLayer attribution="&copy; OpenStreetMap contributors" url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
        {hotspots.map((hotspot) => (
          <Circle
            key={hotspot.label}
            center={[hotspot.latitude, hotspot.longitude]}
            radius={Math.max(700, hotspot.count * 550)}
            pathOptions={{ color: '#B91C1C', fillColor: '#FCA5A5', fillOpacity: 0.24 }}
          />
        ))}
        {complaints.map((complaint) => (
          <Marker key={complaint.id} position={[complaint.latitude, complaint.longitude]} icon={icon}>
            <Popup>
              <strong>{complaint.title}</strong>
              <br />
              {complaint.status} · {complaint.severity}
            </Popup>
          </Marker>
        ))}
      </MapContainer>
    </div>
  );
}
