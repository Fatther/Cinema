import { useState, useEffect, useCallback } from "react";

const API = import.meta.env.VITE_API_URL || "";

const parseError = async (r) => {
  try { const j = await r.json(); throw new Error(j.message || r.statusText); } catch (e) { if (e.message !== r.statusText) throw e; throw new Error(r.statusText); }
};

const api = {
  get: (url) => fetch(`${API}${url}`).then(async r => { if (!r.ok) await parseError(r); return r.json(); }),
  post: (url, body) => fetch(`${API}${url}`, { method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify(body) }).then(async r => { if (!r.ok) await parseError(r); return r.json(); }),
  put: (url, body) => fetch(`${API}${url}`, { method: "PUT", headers: { "Content-Type": "application/json" }, body: JSON.stringify(body) }).then(async r => { if (!r.ok) await parseError(r); return r.json(); }),
  del: (url) => fetch(`${API}${url}`, { method: "DELETE" }).then(async r => { if (!r.ok && r.status !== 204) await parseError(r); }),
};

const Icon = ({ name, size = 16 }) => {
  const icons = {
    film: <><rect x="2" y="2" width="20" height="20" rx="2.18" ry="2.18"/><line x1="7" y1="2" x2="7" y2="22"/><line x1="17" y1="2" x2="17" y2="22"/><line x1="2" y1="12" x2="22" y2="12"/><line x1="2" y1="7" x2="7" y2="7"/><line x1="2" y1="17" x2="7" y2="17"/><line x1="17" y1="17" x2="22" y2="17"/><line x1="17" y1="7" x2="22" y2="7"/></>,
    tag: <><path d="M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 12V2h10l8.59 8.59a2 2 0 0 1 0 2.82z"/><line x1="7" y1="7" x2="7.01" y2="7"/></>,
    building: <><rect x="4" y="2" width="16" height="20"/><rect x="9" y="22" width="6" height="-6"/><line x1="9" y1="7" x2="15" y2="7"/><line x1="9" y1="12" x2="15" y2="12"/></>,
    clock: <><circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/></>,
    ticket: <><path d="M2 9a3 3 0 0 1 0 6v2a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2v-2a3 3 0 0 1 0-6V7a2 2 0 0 0-2-2H4a2 2 0 0 0-2 2z"/></>,
    user: <><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></>,
    plus: <><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></>,
    edit: <><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/></>,
    trash: <><polyline points="3 6 5 6 21 6"/><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/></>,
    search: <><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></>,
    x: <><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></>,
    chevLeft: <polyline points="15 18 9 12 15 6"/>,
    chevRight: <polyline points="9 18 15 12 9 6"/>,
    layers: <><polygon points="12 2 2 7 12 12 22 7 12 2"/><polyline points="2 17 12 22 22 17"/><polyline points="2 12 12 17 22 12"/></>,
  };
  return (
    <svg width={size} height={size} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" style={{ flexShrink: 0 }}>
      {icons[name]}
    </svg>
  );
};

let toastFn = null;

const TOAST_ICONS = {
  success: (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
      <circle cx="12" cy="12" r="10" /><polyline points="9 12 11.5 14.5 16 9.5" />
    </svg>
  ),
  error: (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
      <circle cx="12" cy="12" r="10" /><line x1="15" y1="9" x2="9" y2="15" /><line x1="9" y1="9" x2="15" y2="15" />
    </svg>
  ),
  info: (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
      <circle cx="12" cy="12" r="10" /><line x1="12" y1="8" x2="12" y2="12" /><line x1="12" y1="16" x2="12.01" y2="16" />
    </svg>
  ),
  buy: (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
      <path d="M2 9a3 3 0 0 1 0 6v2a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2v-2a3 3 0 0 1 0-6V7a2 2 0 0 0-2-2H4a2 2 0 0 0-2 2z"/>
    </svg>
  ),
  delete: (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
      <polyline points="3 6 5 6 21 6"/><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/>
    </svg>
  ),
};

const TOAST_STYLES = {
  success: { bg: "#f0fdf4", border: "#86efac", color: "#15803d", iconBg: "#dcfce7" },
  error:   { bg: "#fff1f1", border: "#fca5a5", color: "#b91c1c", iconBg: "#fee2e2" },
  info:    { bg: "#eff6ff", border: "#bfdbfe", color: "#1d4ed8", iconBg: "#dbeafe" },
  buy:     { bg: "#f0fdf4", border: "#6ee7b7", color: "#065f46", iconBg: "#d1fae5" },
  delete:  { bg: "#fff7ed", border: "#fed7aa", color: "#c2410c", iconBg: "#ffedd5" },
};

const Toast = () => {
  const [toasts, setToasts] = useState([]);
  useEffect(() => {
    toastFn = (msg, type = "success") => {
      const id = Date.now();
      setToasts(p => [...p, { id, msg, type }]);
      setTimeout(() => setToasts(p => p.filter(t => t.id !== id)), 3500);
    };
  }, []);
  return (
    <div style={{ position: "fixed", bottom: 28, right: 28, zIndex: 9999, display: "flex", flexDirection: "column", gap: 10, pointerEvents: "none" }}>
      {toasts.map(t => {
        const s = TOAST_STYLES[t.type] ?? TOAST_STYLES.success;
        return (
          <div key={t.id} style={{
            background: s.bg,
            border: `1px solid ${s.border}`,
            color: s.color,
            padding: "12px 16px 12px 12px",
            borderRadius: 13,
            fontSize: 13,
            fontFamily: "inherit",
            fontWeight: 500,
            animation: "toastIn 0.3s cubic-bezier(.21,1.02,.73,1) forwards",
            boxShadow: "0 8px 32px rgba(0,0,0,0.12), 0 2px 8px rgba(0,0,0,0.06)",
            maxWidth: 360,
            minWidth: 220,
            display: "flex",
            alignItems: "center",
            gap: 11,
            letterSpacing: "-0.01em",
          }}>
            <div style={{
              background: s.iconBg,
              borderRadius: 8,
              width: 32, height: 32,
              display: "flex", alignItems: "center", justifyContent: "center",
              flexShrink: 0,
            }}>
              {TOAST_ICONS[t.type] ?? TOAST_ICONS.success}
            </div>
            <span style={{ lineHeight: 1.4 }}>{t.msg}</span>
          </div>
        );
      })}
    </div>
  );
};
const toast = (msg, type) => toastFn?.(msg, type);

const Loader = () => (
  <div style={{ display: "flex", alignItems: "center", justifyContent: "center", padding: "56px 0" }}>
    <div style={{
      width: 36, height: 36, borderRadius: "50%",
      border: "3px solid #e2e8f0",
      borderTopColor: "#3b82f6",
      animation: "spin 0.7s linear infinite",
    }} />
  </div>
);

const Modal = ({ title, onClose, children }) => (
  <div style={{
    position: "fixed", inset: 0,
    background: "rgba(15,23,42,0.45)", backdropFilter: "blur(4px)",
    zIndex: 1000, display: "flex", alignItems: "center", justifyContent: "center", padding: 20,
  }} onClick={onClose}>
    <div style={{
      background: "#ffffff", border: "1px solid #e2e8f0",
      borderRadius: 16, padding: "28px 32px",
      minWidth: 440, maxWidth: 580, width: "100%",
      maxHeight: "88vh", overflowY: "auto",
      boxShadow: "0 20px 60px rgba(0,0,0,0.15)",
      animation: "modalIn 0.2s cubic-bezier(.21,1.02,.73,1)",
    }} onClick={e => e.stopPropagation()}>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 26, paddingBottom: 18, borderBottom: "1px solid #f1f5f9" }}>
        <h2 style={{ margin: 0, fontSize: 17, color: "#0f172a", fontFamily: "'Syne', sans-serif", fontWeight: 700, letterSpacing: "-0.01em" }}>{title}</h2>
        <button onClick={onClose} style={{ background: "#f8fafc", border: "1px solid #e2e8f0", borderRadius: 8, color: "#94a3b8", cursor: "pointer", padding: "5px 7px", display: "flex", lineHeight: 1 }}>
          <Icon name="x" size={14} />
        </button>
      </div>
      {children}
    </div>
  </div>
);

const Field = ({ label, children }) => (
  <div style={{ marginBottom: 18 }}>
    <label style={{ display: "block", fontSize: 11, color: "#64748b", marginBottom: 7, textTransform: "uppercase", letterSpacing: "0.09em", fontWeight: 700 }}>{label}</label>
    {children}
  </div>
);

const inputStyle = {
  width: "100%", background: "#f8fafc", border: "1px solid #e2e8f0",
  color: "#0f172a", padding: "10px 13px", borderRadius: 9, fontSize: 13,
  outline: "none", fontFamily: "inherit", boxSizing: "border-box",
};

const btnStyle = (variant = "primary") => ({
  padding: "9px 18px", borderRadius: 9,
  border: variant === "ghost" ? "1px solid #e2e8f0" : "none",
  cursor: "pointer", fontSize: 13, fontWeight: 600, fontFamily: "inherit",
  background: variant === "primary"
    ? "linear-gradient(135deg,#1d4ed8,#3b82f6)"
    : variant === "danger" ? "#dc2626" : "#f8fafc",
  color: variant === "ghost" ? "#64748b" : "#fff",
  display: "inline-flex", alignItems: "center", gap: 7,
  whiteSpace: "nowrap",
});

const Badge = ({ label }) => (
  <span style={{
    background: "#eff6ff", color: "#2563eb", border: "1px solid #bfdbfe",
    padding: "2px 8px", borderRadius: 5, fontSize: 11, fontWeight: 600,
    display: "inline-block", marginRight: 3, marginBottom: 3, whiteSpace: "nowrap",
  }}>{label}</span>
);

const Pagination = ({ meta, onPage }) => {
  if (!meta || meta.totalPage <= 1) return null;
  return (
    <div style={{ display: "flex", alignItems: "center", gap: 10, padding: "14px 20px 6px", justifyContent: "center", borderTop: "1px solid #f1f5f9" }}>
      <button style={{ ...btnStyle("ghost"), padding: "6px 11px" }} disabled={meta.page === 0} onClick={() => onPage(meta.page - 1)}><Icon name="chevLeft" size={13} /></button>
      <span style={{ color: "#94a3b8", fontSize: 12, minWidth: 100, textAlign: "center" }}>
        стр. <b style={{ color: "#475569" }}>{meta.page + 1}</b> / <b style={{ color: "#475569" }}>{meta.totalPage}</b>
      </span>
      <button style={{ ...btnStyle("ghost"), padding: "6px 11px" }} disabled={meta.page >= meta.totalPage - 1} onClick={() => onPage(meta.page + 1)}><Icon name="chevRight" size={13} /></button>
    </div>
  );
};

const CELL = { padding: "14px 20px", textAlign: "center", verticalAlign: "middle" };

const Table = ({ cols, rows, onEdit, onDelete }) => (
  <div style={{ width: "100%", overflowX: "auto" }}>
    <table style={{ width: "100%", borderCollapse: "collapse", fontSize: 15, tableLayout: "fixed" }}>
      <colgroup>
        {cols.map(c => <col key={c.key} style={{ width: c.width }} />)}
        <col style={{ width: 100 }} />
      </colgroup>
      <thead>
        <tr style={{ background: "#f8fafc" }}>
          {cols.map(c => (
            <th key={c.key} style={{
              ...CELL,
              color: "#94a3b8", fontWeight: 700, fontSize: 10,
              textTransform: "uppercase", letterSpacing: "0.1em",
              borderBottom: "1px solid #e2e8f0",
              whiteSpace: "nowrap", overflow: "hidden", textOverflow: "ellipsis",
            }}>{c.label}</th>
          ))}
          <th style={{
            ...CELL,
            color: "#94a3b8", fontWeight: 700, fontSize: 10,
            textTransform: "uppercase", letterSpacing: "0.1em",
            borderBottom: "1px solid #e2e8f0", whiteSpace: "nowrap",
          }}>Действия</th>
        </tr>
      </thead>
      <tbody>
        {rows.length === 0 && (
          <tr>
            <td colSpan={cols.length + 1} style={{ padding: "48px 16px", textAlign: "center", color: "#cbd5e1", fontSize: 13 }}>
              — нет данных —
            </td>
          </tr>
        )}
        {rows.map((row, i) => (
          <tr key={row.id ?? i}
            style={{ borderBottom: "1px solid #f1f5f9", transition: "background 0.1s" }}
            onMouseEnter={e => e.currentTarget.style.background = "#f8fafc"}
            onMouseLeave={e => e.currentTarget.style.background = "transparent"}
          >
            {cols.map(c => (
              <td key={c.key} style={{ ...CELL, color: "#475569", overflow: "hidden" }}>
                {c.render
                  ? c.render(row)
                  : <span style={{ display: "block", overflow: "hidden", textOverflow: "ellipsis", whiteSpace: "nowrap" }}>
                      {String(row[c.key] ?? "—")}
                    </span>
                }
              </td>
            ))}
            <td style={{ ...CELL }}>
              <div style={{ display: "inline-flex", gap: 6 }}>
                <button onClick={() => onEdit(row)} title="Редактировать"
                  style={{ background: "#eff6ff", border: "1px solid #bfdbfe", borderRadius: 7, color: "#2563eb", cursor: "pointer", padding: "6px 8px", display: "flex" }}
                  onMouseEnter={e => e.currentTarget.style.background = "#dbeafe"}
                  onMouseLeave={e => e.currentTarget.style.background = "#eff6ff"}
                ><Icon name="edit" size={13} /></button>
                <button onClick={() => onDelete(row)} title="Удалить"
                  style={{ background: "#fff1f2", border: "1px solid #fecdd3", borderRadius: 7, color: "#e11d48", cursor: "pointer", padding: "6px 8px", display: "flex" }}
                  onMouseEnter={e => e.currentTarget.style.background = "#ffe4e6"}
                  onMouseLeave={e => e.currentTarget.style.background = "#fff1f2"}
                ><Icon name="trash" size={13} /></button>
              </div>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  </div>
);

const Section = ({ title, icon, onAdd, children }) => (
  <div>
    <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 20 }}>
      <div style={{ display: "flex", alignItems: "center", gap: 12 }}>
        <div style={{ background: "#eff6ff", border: "1px solid #bfdbfe", borderRadius: 10, padding: "8px 9px", display: "flex", alignItems: "center", justifyContent: "center" }}>
          <span style={{ color: "#2563eb", display: "flex", alignItems: "center", justifyContent: "center" }}><Icon name={icon} size={17} /></span>
        </div>
        <h2 style={{ margin: 0, fontSize: 26, fontFamily: "'Syne', sans-serif", fontWeight: 800, color: "#0f172a", letterSpacing: "-0.02em" }}>{title}</h2>
      </div>
      <button style={btnStyle()} onClick={onAdd}><Icon name="plus" size={13} />Добавить</button>
    </div>
    <div style={{ background: "#ffffff", border: "1px solid #e2e8f0", borderRadius: 14, overflow: "hidden", boxShadow: "0 2px 16px rgba(0,0,0,0.06)", width: "100%", minWidth: 0 }}>
      {children}
    </div>
  </div>
);

const SearchBar = ({ value, onChange, onSearch, onReset, placeholder }) => (
  <div style={{ display: "flex", gap: 8, padding: "16px 16px 0" }}>
    <div style={{ position: "relative", flex: 1, maxWidth: 300 }}>
      <span style={{ position: "absolute", left: 11, top: "50%", transform: "translateY(-50%)", color: "#cbd5e1", pointerEvents: "none" }}>
        <Icon name="search" size={14} />
      </span>
      <input style={{ ...inputStyle, paddingLeft: 34 }} value={value} onChange={e => onChange(e.target.value)}
        placeholder={placeholder} onKeyDown={e => e.key === "Enter" && onSearch()} />
    </div>
    <button style={btnStyle()} onClick={onSearch}><Icon name="search" size={13} />Найти</button>
    {onReset && <button style={btnStyle("ghost")} onClick={onReset}>Сбросить</button>}
  </div>
);

const CardDivider = () => <div style={{ height: 1, background: "#f1f5f9", margin: "14px 0 0" }} />;

const ModalActions = ({ onCancel, onSave }) => (
  <div style={{ display: "flex", gap: 10, justifyContent: "flex-end", marginTop: 24, paddingTop: 18, borderTop: "1px solid #f1f5f9" }}>
    <button style={btnStyle("ghost")} onClick={onCancel}>Отмена</button>
    <button style={btnStyle()} onClick={onSave}>Сохранить</button>
  </div>
);

const Genres = () => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modal, setModal] = useState(null);
  const [form, setForm] = useState({ name: "" });
  const load = useCallback(() => { setLoading(true); api.get("/genres").then(setData).catch(() => toast("Ошибка загрузки жанров", "error")).finally(() => setLoading(false)); }, []);
  useEffect(() => { load(); }, [load]);
  const openCreate = () => { setForm({ name: "" }); setModal({ mode: "create" }); };
  const openEdit = (item) => { setForm({ name: item.name }); setModal({ mode: "edit", item }); };
  const submit = async () => {
    try {
      if (modal.mode === "create") await api.post("/genres/post", form);
      else await api.put(`/genres/update/${modal.item.id}`, form);
      toast(modal.mode === "create" ? `Жанр «${form.name}» добавлен` : `Жанр «${form.name}» обновлён`); setModal(null); load();
    } catch { toast("Ошибка сохранения жанра", "error"); }
  };
  const del = async (item) => {
    if (!confirm(`Удалить жанр «${item.name}»?`)) return;
    try { await api.del(`/genres/delete/${item.id}`); toast(`Жанр «${item.name}» удалён`, "delete"); load(); } catch { toast("Ошибка удаления жанра", "error"); }
  };
  return (
    <Section title="Жанры" icon="tag" onAdd={openCreate}>
      {loading ? <Loader /> : <Table cols={[{ key: "name", label: "Название", width: "100%" }]} rows={data} onEdit={openEdit} onDelete={del} />}
      {modal && (<Modal title={modal.mode === "create" ? "Новый жанр" : "Редактировать жанр"} onClose={() => setModal(null)}>
        <Field label="Название"><input style={inputStyle} value={form.name} onChange={e => setForm({ name: e.target.value })} placeholder="Боевик" maxLength={20} /></Field>
        <ModalActions onCancel={() => setModal(null)} onSave={submit} />
      </Modal>)}
    </Section>
  );
};

const Halls = () => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modal, setModal] = useState(null);
  const [form, setForm] = useState({ name: "", price: "", seatAmount: "" });
  const load = useCallback(() => { setLoading(true); api.get("/halls").then(setData).catch(() => toast("Ошибка загрузки залов", "error")).finally(() => setLoading(false)); }, []);
  useEffect(() => { load(); }, [load]);
  const openCreate = () => { setForm({ name: "", price: "", seatAmount: "" }); setModal({ mode: "create" }); };
  const openEdit = (item) => { setForm({ name: item.name, price: item.price, seatAmount: item.seatAmount }); setModal({ mode: "edit", item }); };
  const submit = async () => {
    try {
      const body = { name: form.name, price: Number(form.price), seatAmount: Number(form.seatAmount) };
      if (modal.mode === "create") await api.post("/halls/post", body); else await api.put(`/halls/update/${modal.item.id}`, body);
      toast(modal.mode === "create" ? `Зал «${form.name}» добавлен` : `Зал «${form.name}» обновлён`); setModal(null); load();
    } catch { toast("Ошибка сохранения зала", "error"); }
  };
  const del = async (item) => {
    if (!confirm(`Удалить зал «${item.name}»?`)) return;
    try { await api.del(`/halls/delete/${item.id}`); toast(`Зал «${item.name}» удалён`, "delete"); load(); } catch { toast("Ошибка удаления зала", "error"); }
  };
  return (
    <Section title="Залы" icon="building" onAdd={openCreate}>
      {loading ? <Loader /> : <Table cols={[
        { key: "name", label: "Название", width: "100%" },
        { key: "price", label: "Цена", width: 120, render: r => <span style={{ color: "#0f172a", fontWeight: 700 }}>{r.price} р.</span> },
        { key: "seatAmount", label: "Мест", width: 100 },
      ]} rows={data} onEdit={openEdit} onDelete={del} />}
      {modal && (<Modal title={modal.mode === "create" ? "Новый зал" : "Редактировать зал"} onClose={() => setModal(null)}>
        <Field label="Название"><input style={inputStyle} value={form.name} onChange={e => setForm(p => ({ ...p, name: e.target.value }))} placeholder="Зал 1" maxLength={20} /></Field>
        <Field label="Цена (р.)"><input style={inputStyle} type="number" value={form.price} onChange={e => setForm(p => ({ ...p, price: e.target.value }))} placeholder="350" /></Field>
        <Field label="Кол-во мест"><input style={inputStyle} type="number" value={form.seatAmount} onChange={e => setForm(p => ({ ...p, seatAmount: e.target.value }))} placeholder="100" /></Field>
        <ModalActions onCancel={() => setModal(null)} onSave={submit} />
      </Modal>)}
    </Section>
  );
};

const Movies = () => {
  const [data, setData] = useState([]);
  const [meta, setMeta] = useState(null);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(true);
  const [modal, setModal] = useState(null);
  const [genres, setGenres] = useState([]);
  const [form, setForm] = useState({ title: "", duration: "", genreIds: [] });
  const load = useCallback(() => { setLoading(true);
    api.get(`/movies?page=${page}&size=10`).then(r => { setData(r.content); setMeta(r.metadata); }).catch(() => toast("Ошибка загрузки фильмов", "error")).finally(() => setLoading(false)); }, [page]);
  useEffect(() => { load(); }, [load]);
  useEffect(() => { api.get("/genres").then(setGenres).catch(() => {}); }, []);
  const openCreate = () => { setForm({ title: "", duration: "", genreIds: [] }); setModal({ mode: "create" }); };
  const openEdit = (item) => {
    const genreIds = genres.filter(g => item.genres?.includes(g.name)).map(g => g.id);
    setForm({ title: item.title, duration: item.duration, genreIds }); setModal({ mode: "edit", item });
  };
  const toggleGenre = (id) => setForm(p => ({ ...p, genreIds: p.genreIds.includes(id) ? p.genreIds.filter(x => x !== id) : [...p.genreIds, id] }));
  const submit = async () => {
    try {
      const body = { title: form.title, duration: Number(form.duration), genreIds: form.genreIds };
      if (modal.mode === "create") await api.post("/movies/post", body); else await api.put(`/movies/update/${modal.item.id}`, body);
      toast(modal.mode === "create" ? `Фильм «${form.title}» добавлен` : `Фильм «${form.title}» обновлён`); setModal(null); load();
    } catch { toast("Ошибка сохранения фильма", "error"); }
  };
  const del = async (item) => {
    if (!confirm(`Удалить фильм «${item.title}»?`)) return;
    try { await api.del(`/movies/delete/${item.id}`); toast(`Фильм «${item.title}» удалён`, "delete"); load(); } catch { toast("Ошибка удаления фильма", "error"); }
  };
  return (
    <Section title="Фильмы" icon="film" onAdd={openCreate}>
      {loading ? <Loader /> : <>
        <Table cols={[
          { key: "title", label: "Название", width: 240 },
          { key: "duration", label: "Длительность", width: 140, render: r => <span style={{ color: "#64748b" }}>{r.duration} мин</span> },
          { key: "genres", label: "Жанры", width: "100%", render: r => <div style={{ display: "flex", flexWrap: "wrap", gap: 3, justifyContent: "center" }}>{(r.genres || []).map(g => <Badge key={g} label={g} />)}</div> },
        ]} rows={data} onEdit={openEdit} onDelete={del} />
        <Pagination meta={meta} onPage={setPage} />
      </>}
      {modal && (<Modal title={modal.mode === "create" ? "Новый фильм" : "Редактировать фильм"} onClose={() => setModal(null)}>
        <Field label="Название"><input style={inputStyle} value={form.title} onChange={e => setForm(p => ({ ...p, title: e.target.value }))} placeholder="Название фильма" maxLength={50} /></Field>
        <Field label="Длительность (мин)"><input style={inputStyle} type="number" value={form.duration} onChange={e => setForm(p => ({ ...p, duration: e.target.value }))} placeholder="120" /></Field>
        <Field label="Жанры">
          <div style={{ display: "flex", flexWrap: "wrap", gap: 8, padding: "10px 14px", background: "#f8fafc", border: "1px solid #e2e8f0", borderRadius: 9 }}>
            {genres.length === 0 && <span style={{ color: "#cbd5e1", fontSize: 12 }}>Нет жанров</span>}
            {genres.map(g => (
              <label key={g.id} style={{ display: "flex", alignItems: "center", gap: 7, cursor: "pointer", color: form.genreIds.includes(g.id) ? "#2563eb" : "#94a3b8", fontSize: 13, userSelect: "none" }}>
                <input type="checkbox" checked={form.genreIds.includes(g.id)} onChange={() => toggleGenre(g.id)} style={{ accentColor: "#3b82f6", width: 15, height: 15, cursor: "pointer" }} />{g.name}
              </label>
            ))}
          </div>
        </Field>
        <ModalActions onCancel={() => setModal(null)} onSave={submit} />
      </Modal>)}
    </Section>
  );
};

const Sessions = () => {
  const [data, setData] = useState([]);
  const [meta, setMeta] = useState(null);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(true);
  const [modal, setModal] = useState(null);
  const [buyModal, setBuyModal] = useState(null);
  const [movies, setMovies] = useState([]);
  const [halls, setHalls] = useState([]);
  const [visitors, setVisitors] = useState([]);
  const [search, setSearch] = useState("");
  const [searchInput, setSearchInput] = useState("");
  const [form, setForm] = useState({ startTime: "", movieId: "", hallId: "" });
  const [buyForm, setBuyForm] = useState({ visitorId: "", seatNumber: "" });
  const load = useCallback(() => {
    setLoading(true);
    const url = search ? `/sessions/search?title=${encodeURIComponent(search)}&page=${page}&size=10` : `/sessions?page=${page}&size=10`;
    api.get(url).then(r => { setData(r.content); setMeta(r.metadata); }).catch(() => toast("Ошибка загрузки сеансов", "error")).finally(() => setLoading(false));
  }, [page, search]);
  useEffect(() => { load(); }, [load]);
  useEffect(() => {
    api.get("/movies?size=100").then(r => setMovies(r.content)).catch(() => {});
    api.get("/halls").then(setHalls).catch(() => {});
    api.get("/visitors").then(setVisitors).catch(() => {});
  }, []);
  const openCreate = () => { setForm({ startTime: "", movieId: "", hallId: "" }); setModal({ mode: "create" }); };
  const openEdit = (item) => {
    const movie = movies.find(m => m.title === item.movieTitle);
    const hall = halls.find(h => h.name === item.hallName);
    const dt = item.startTime ? item.startTime.replace(" ", "T").slice(0, 16) : "";
    setForm({ startTime: dt, movieId: movie?.id ?? "", hallId: hall?.id ?? "" }); setModal({ mode: "edit", item });
  };
  const openBuy = (item) => { setBuyForm({ visitorId: "", seatNumber: "" }); setBuyModal(item); };
  const submit = async () => {
    try {
      const body = { startTime: form.startTime + ":00", movieId: Number(form.movieId), hallId: Number(form.hallId) };
      if (modal.mode === "create") await api.post("/sessions/post", body); else await api.put(`/sessions/update/${modal.item.id}`, body);
      toast(modal.mode === "create" ? "Сеанс добавлен" : "Сеанс обновлён"); setModal(null); load();
    } catch { toast("Ошибка сохранения сеанса", "error"); }
  };
  const submitBuy = async () => {
    try {
      const body = { sessionId: Number(buyModal.id), visitorId: Number(buyForm.visitorId), seatNumber: Number(buyForm.seatNumber) };
      await api.post("/tickets/post", body);
      toast(`Билет на «${buyModal.movieTitle}» куплен`, "buy"); setBuyModal(null);
    } catch (e) { toast(e.message || "Ошибка покупки билета", "error"); }
  };
  const del = async (item) => {
    if (!confirm("Удалить сеанс?")) return;
    try { await api.del(`/sessions/delete/${item.id}`); toast("Сеанс удалён", "delete"); load(); } catch { toast("Ошибка удаления сеанса", "error"); }
  };
  const handleSearch = () => { setSearch(searchInput); setPage(0); };
  const currentHall = buyModal ? halls.find(h => h.name === buyModal.hallName) : null;
  const maxSeats = currentHall?.seatAmount || 0;

  return (
    <Section title="Сеансы" icon="clock" onAdd={openCreate}>
      <SearchBar value={searchInput} onChange={setSearchInput} onSearch={handleSearch}
        onReset={search ? () => { setSearch(""); setSearchInput(""); setPage(0); } : null} placeholder="Поиск по фильму..." />
      <CardDivider />
      {loading ? <Loader /> : <>
        <div style={{ width: "100%", overflowX: "auto" }}>
          <table style={{ width: "100%", borderCollapse: "collapse", fontSize: 15, tableLayout: "fixed" }}>
            <colgroup>
              <col style={{ width: 120 }} />
              <col style={{ width: "100%" }} />
              <col style={{ width: 140 }} />
              <col style={{ width: 100 }} />
              <col style={{ width: 130 }} />
              <col style={{ width: 100 }} />
            </colgroup>
            <thead>
              <tr style={{ background: "#f8fafc" }}>
                {[
                  { label: "Начало" }, { label: "Фильм" }, { label: "Зал" },
                  { label: "Цена" }, { label: "Купить билет" }, { label: "Действия" }
                ].map(c => (
                  <th key={c.label} style={{ ...CELL, color: "#94a3b8", fontWeight: 700, fontSize: 10, textTransform: "uppercase", letterSpacing: "0.1em", borderBottom: "1px solid #e2e8f0", whiteSpace: "nowrap" }}>{c.label}</th>
                ))}
              </tr>
            </thead>
            <tbody>
              {data.length === 0 && (
                <tr><td colSpan={6} style={{ padding: "48px 16px", textAlign: "center", color: "#cbd5e1", fontSize: 13 }}>— нет данных —</td></tr>
              )}
              {data.map((row, i) => {
                const dt = row.startTime?.replace("T", " ").slice(0, 16) ?? "";
                const [date, time] = dt.split(" ");
                return (
                  <tr key={row.id ?? i} style={{ borderBottom: "1px solid #f1f5f9", transition: "background 0.1s" }}
                    onMouseEnter={e => e.currentTarget.style.background = "#f8fafc"}
                    onMouseLeave={e => e.currentTarget.style.background = "transparent"}
                  >
                    <td style={{ ...CELL, color: "#475569" }}>
                      <div style={{ fontVariantNumeric: "tabular-nums", lineHeight: 1.5 }}>
                        <div style={{ color: "#0f172a", fontWeight: 600 }}>{time}</div>
                        <div style={{ color: "#94a3b8", fontSize: 11 }}>{date}</div>
                      </div>
                    </td>
                    <td style={{ ...CELL, color: "#475569", overflow: "hidden" }}>
                      <span style={{ display: "block", overflow: "hidden", textOverflow: "ellipsis", whiteSpace: "nowrap" }}>{row.movieTitle ?? "—"}</span>
                    </td>
                    <td style={{ ...CELL, color: "#475569" }}>{row.hallName ?? "—"}</td>
                    <td style={{ ...CELL }}><span style={{ color: "#0f172a", fontWeight: 700 }}>{row.price} р.</span></td>
                    <td style={{ ...CELL }}>
                      <button onClick={() => openBuy(row)}
                        style={{ background: "linear-gradient(135deg,#059669,#10b981)", border: "none", borderRadius: 8, color: "#fff", cursor: "pointer", padding: "7px 13px", fontSize: 12, fontWeight: 600, fontFamily: "inherit", display: "inline-flex", alignItems: "center", gap: 5, whiteSpace: "nowrap" }}
                        onMouseEnter={e => e.currentTarget.style.opacity = "0.88"}
                        onMouseLeave={e => e.currentTarget.style.opacity = "1"}
                      >
                        <Icon name="ticket" size={12} />Купить
                      </button>
                    </td>
                    <td style={{ ...CELL }}>
                      <div style={{ display: "inline-flex", gap: 6 }}>
                        <button onClick={() => openEdit(row)} title="Редактировать"
                          style={{ background: "#eff6ff", border: "1px solid #bfdbfe", borderRadius: 7, color: "#2563eb", cursor: "pointer", padding: "6px 8px", display: "flex" }}
                          onMouseEnter={e => e.currentTarget.style.background = "#dbeafe"}
                          onMouseLeave={e => e.currentTarget.style.background = "#eff6ff"}
                        ><Icon name="edit" size={13} /></button>
                        <button onClick={() => del(row)} title="Удалить"
                          style={{ background: "#fff1f2", border: "1px solid #fecdd3", borderRadius: 7, color: "#e11d48", cursor: "pointer", padding: "6px 8px", display: "flex" }}
                          onMouseEnter={e => e.currentTarget.style.background = "#ffe4e6"}
                          onMouseLeave={e => e.currentTarget.style.background = "#fff1f2"}
                        ><Icon name="trash" size={13} /></button>
                      </div>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
        <Pagination meta={meta} onPage={setPage} />
      </>}
      {modal && (<Modal title={modal.mode === "create" ? "Новый сеанс" : "Редактировать сеанс"} onClose={() => setModal(null)}>
        <Field label="Дата и время"><input style={inputStyle} type="datetime-local" value={form.startTime} onChange={e => setForm(p => ({ ...p, startTime: e.target.value }))} /></Field>
        <Field label="Фильм"><select style={inputStyle} value={form.movieId} onChange={e => setForm(p => ({ ...p, movieId: e.target.value }))}><option value="">— выбрать —</option>{movies.map(m => <option key={m.id} value={m.id}>{m.title}</option>)}</select></Field>
        <Field label="Зал"><select style={inputStyle} value={form.hallId} onChange={e => setForm(p => ({ ...p, hallId: e.target.value }))}><option value="">— выбрать —</option>{halls.map(h => <option key={h.id} value={h.id}>{h.name} ({h.price} р.)</option>)}</select></Field>
        <ModalActions onCancel={() => setModal(null)} onSave={submit} />
      </Modal>)}
        {buyModal && (<Modal title="Купить билет" onClose={() => setBuyModal(null)}>
            <div style={{ background: "#f0fdf4", border: "1px solid #bbf7d0", borderRadius: 9, padding: "10px 14px", marginBottom: 18, fontSize: 13, color: "#15803d" }}>
                <div style={{ fontWeight: 700, marginBottom: 4 }}>{buyModal.movieTitle}</div>
                {buyModal.startTime?.replace("T", " · ").slice(0, 18)} · {buyModal.hallName} · {buyModal.price} р.
            </div>
            <Field label="Посетитель">
                <select style={inputStyle} value={buyForm.visitorId} onChange={e => setBuyForm(p => ({ ...p, visitorId: e.target.value }))}>
                    <option value="">— выбрать —</option>
                    {visitors.map(v => <option key={v.id} value={v.id}>{v.name} ({v.email})</option>)}
                </select>
            </Field>

            {}
            <Field label={"Номер места"}>
                <input
                    style={inputStyle}
                    type="number"
                    value={buyForm.seatNumber}
                    onChange={e => {
                        const val = e.target.value;
                        if (maxSeats && parseInt(val) > maxSeats) return;
                        setBuyForm(p => ({ ...p, seatNumber: val }));
                    }}
                    placeholder={maxSeats ? `1–${maxSeats}` : "1"}
                    min="1"
                    max={maxSeats || undefined}
                />
            </Field>

            <ModalActions onCancel={() => setBuyModal(null)} onSave={submitBuy} />
        </Modal>)}
    </Section>
  );
};

const Visitors = () => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modal, setModal] = useState(null);
  const [form, setForm] = useState({ name: "", email: "" });
  const load = useCallback(() => { setLoading(true); api.get("/visitors").then(setData).catch(() => toast("Ошибка загрузки посетителей", "error")).finally(() => setLoading(false)); }, []);
  useEffect(() => { load(); }, [load]);
  const openCreate = () => { setForm({ name: "", email: "" }); setModal({ mode: "create" }); };
  const openEdit = (item) => { setForm({ name: item.name, email: item.email }); setModal({ mode: "edit", item }); };
  const submit = async () => {
    try {
      if (modal.mode === "create") await api.post("/visitors/post", form); else await api.put(`/visitors/update/${modal.item.id}`, form);
      toast(modal.mode === "create" ? `Посетитель «${form.name}» добавлен` : `Посетитель «${form.name}» обновлён`); setModal(null); load();
    } catch { toast("Ошибка сохранения посетителя", "error"); }
  };
  const del = async (item) => {
    if (!confirm(`Удалить посетителя «${item.name}»?`)) return;
    try { await api.del(`/visitors/delete/${item.id}`); toast(`Посетитель «${item.name}» удалён`, "delete"); load(); } catch { toast("Ошибка удаления посетителя", "error"); }
  };
  return (
    <Section title="Посетители" icon="user" onAdd={openCreate}>
      {loading ? <Loader /> : <Table cols={[
        { key: "name", label: "Имя", width: 200 },
        { key: "email", label: "Email", width: "100%" },
      ]} rows={data} onEdit={openEdit} onDelete={del} />}
      {modal && (<Modal title={modal.mode === "create" ? "Новый посетитель" : "Редактировать посетителя"} onClose={() => setModal(null)}>
        <Field label="Имя"><input style={inputStyle} value={form.name} onChange={e => setForm(p => ({ ...p, name: e.target.value }))} placeholder="Иван Иванов" maxLength={20} /></Field>
        <Field label="Email"><input style={inputStyle} type="email" value={form.email} onChange={e => setForm(p => ({ ...p, email: e.target.value }))} placeholder="ivan@example.com" maxLength={30} /></Field>
        <ModalActions onCancel={() => setModal(null)} onSave={submit} />
      </Modal>)}
    </Section>
  );
};

const Tickets = () => {
  const [data, setData] = useState([]);
  const [meta, setMeta] = useState(null);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(true);
  const [modal, setModal] = useState(null);
  const [sessions, setSessions] = useState([]);
  const [visitors, setVisitors] = useState([]);
  const [halls, setHalls] = useState([]);
  const [search, setSearch] = useState("");
  const [searchInput, setSearchInput] = useState("");
  const [form, setForm] = useState({ sessionId: "", visitorId: "", seatNumber: "" });
  const load = useCallback(() => {
    setLoading(true);
    const url = search ? `/tickets/search?visitorName=${encodeURIComponent(search)}&page=${page}&size=10` : `/tickets?page=${page}&size=10`;
    api.get(url).then(r => { setData(r.content); setMeta(r.metadata); }).catch(() => toast("Ошибка загрузки билетов", "error")).finally(() => setLoading(false));
  }, [page, search]);
  useEffect(() => { load(); }, [load]);
  useEffect(() => { api.get("/sessions?size=100").then(r => setSessions(r.content)).catch(() => {}); api.get("/visitors").then(setVisitors).catch(() => {}); api.get("/halls").then(setHalls).catch(() => {}); }, []);
  const openCreate = () => { setForm({ sessionId: "", visitorId: "", seatNumber: "" }); setModal({ mode: "create" }); };
    const openEdit = async (item) => {
        const session = sessions.find(s => s.id === item.session?.id);
        let visitorList = visitors;
        if (visitorList.length === 0) {
            try { visitorList = await api.get("/visitors"); setVisitors(visitorList); } catch { visitorList = []; }
        }
        const emailFromStr = item.visitorEmail?.match(/\(([^)]+)\)/)?.[1] ?? item.visitorEmail;
        const visitor = visitorList.find(v => v.email === emailFromStr);
        setForm({
            sessionId: String(session?.id ?? ""),
            visitorId: String(visitor?.id ?? ""),
            seatNumber: item.seatNumber
        });
        setModal({ mode: "edit", item });
    };
    const submit = async () => {
    try {
      const body = { sessionId: Number(form.sessionId), visitorId: Number(form.visitorId), seatNumber: Number(form.seatNumber) };
      if (modal.mode === "create") await api.post("/tickets/post", body); else await api.put(`/tickets/update/${modal.item.id}`, body);
      toast(modal.mode === "create" ? "Билет добавлен" : "Билет обновлён"); setModal(null); load();
    } catch (e) { toast(e.message || "Ошибка сохранения билета", "error"); }
  };
  const del = async (item) => {
    if (!confirm("Удалить билет?")) return;
    try { await api.del(`/tickets/delete/${item.id}`); toast("Билет удалён", "delete"); load(); } catch (e) { toast(e.message || "Ошибка удаления билета", "error"); }
  };
  const handleSearch = () => { setSearch(searchInput); setPage(0); };
  const selectedSession = sessions.find(s => String(s.id) === String(form.sessionId));
  const selectedHall = selectedSession ? halls.find(h => h.name === selectedSession.hallName) : null;
  const maxSeats = selectedHall?.seatAmount || 0;
  return (
    <Section title="Билеты" icon="ticket" onAdd={openCreate}>
      <SearchBar value={searchInput} onChange={setSearchInput} onSearch={handleSearch}
        onReset={search ? () => { setSearch(""); setSearchInput(""); setPage(0); } : null} placeholder="Поиск по посетителю..." />
      <CardDivider />
      {loading ? <Loader /> : <>
        <Table cols={[
          { key: "seatNumber", label: "Место", width: 100 },
          { key: "session", label: "Фильм", width: 220, render: r => r.session
            ? <span style={{ color: "#334155" }}>{r.session.movieTitle}</span>
            : "—"
          },
          { key: "sessionTime", label: "Дата сеанса", width: 160, render: r => {
            const dt = r.session?.startTime?.slice(0, 16).replace("T", " ") ?? "";
            const [date, time] = dt.split(" ");
            return dt ? <div style={{ fontVariantNumeric: "tabular-nums", lineHeight: 1.5 }}>
              <div style={{ color: "#0f172a", fontWeight: 600 }}>{time}</div>
              <div style={{ color: "#94a3b8", fontSize: 11 }}>{date}</div>
            </div> : <span style={{ color: "#cbd5e1" }}>—</span>;
          }},
          { key: "visitorEmail", label: "Посетитель", width: "100%" },
        ]} rows={data} onEdit={openEdit} onDelete={del} />
        <Pagination meta={meta} onPage={setPage} />
      </>}
        {modal && (
            <Modal title={modal.mode === "create" ? "Новый билет" : "Редактировать билет"} onClose={() => setModal(null)}>
                <Field label="Сеанс">
                    <select style={inputStyle} value={form.sessionId} onChange={e => setForm(p => ({ ...p, sessionId: e.target.value }))}>
                        <option value="">— выбрать —</option>
                        {sessions.map(s => (
                            <option key={s.id} value={String(s.id)}>
                                {s.movieTitle} | {s.startTime?.slice(0, 16).replace("T", " ")} | {s.hallName}
                            </option>
                        ))}
                    </select>
                </Field>
                <Field label="Посетитель">
                    <select style={inputStyle} value={form.visitorId} onChange={e => setForm(p => ({ ...p, visitorId: e.target.value }))}>
                        <option value="">— выбрать —</option>
                        {visitors.map(v => (
                            <option key={v.id} value={String(v.id)}>
                                {v.name} ({v.email})
                            </option>
                        ))}
                    </select>
                </Field>
                <Field label={"Номер места"}>
                    <input
                        style={inputStyle}
                        type="number"
                        value={form.seatNumber}
                        onChange={e => {
                            const val = e.target.value;
                            if (maxSeats && parseInt(val) > maxSeats) return;
                            setForm(p => ({ ...p, seatNumber: val }));
                        }}
                        placeholder={maxSeats ? `1–${maxSeats}` : "1"}
                        min="1"
                        max={maxSeats || undefined}
                    />
                </Field>
                <ModalActions onCancel={() => setModal(null)} onSave={submit} />
            </Modal>
        )}
    </Section>
  );
};

const NAV = [
  { id: "movies",   label: "Фильмы",     icon: "film" },
  { id: "sessions", label: "Сеансы",     icon: "clock" },
  { id: "tickets",  label: "Билеты",     icon: "ticket" },
  { id: "visitors", label: "Посетители", icon: "user" },
  { id: "genres",   label: "Жанры",      icon: "tag" },
  { id: "halls",    label: "Залы",        icon: "building" },
];

export default function App() {
  const [active, setActive] = useState("movies");
  const pages = { genres: <Genres />, halls: <Halls />, movies: <Movies />, sessions: <Sessions />, visitors: <Visitors />, tickets: <Tickets /> };

  return (
    <>
      <style>{`
        @import url('https://fonts.googleapis.com/css2?family=Syne:wght@700;800&family=Inter:wght@400;500;600&display=swap');
        *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
        html, body, #root { width: 100%; min-height: 100vh; font-size: 16px; }
        body { background: #f1f5f9; color: #0f172a; font-family: 'Inter', sans-serif; -webkit-font-smoothing: antialiased; }
        ::-webkit-scrollbar { width: 5px; height: 5px; }
        ::-webkit-scrollbar-track { background: transparent; }
        ::-webkit-scrollbar-thumb { background: #cbd5e1; border-radius: 4px; }
        select option { background: #ffffff; color: #0f172a; }
        input[type="datetime-local"]::-webkit-calendar-picker-indicator { filter: none; }
        input:focus, select:focus { border-color: #3b82f6 !important; box-shadow: 0 0 0 3px rgba(59,130,246,0.12); }
        input[type="number"]::-webkit-inner-spin-button,
        input[type="number"]::-webkit-outer-spin-button {
          opacity: 1;
          background: #eff6ff;
          border-left: 1px solid #bfdbfe;
          border-radius: 0 8px 8px 0;
          cursor: pointer;
          filter: none;
        }
        input[type="number"] { -moz-appearance: auto; }
        @keyframes toastIn { from { opacity:0; transform:translateX(24px) scale(0.96); } to { opacity:1; transform:translateX(0) scale(1); } }
        @keyframes modalIn { from { opacity:0; transform:translateY(10px) scale(0.98); } to { opacity:1; transform:translateY(0) scale(1); } }
        @keyframes spin { to { transform: rotate(360deg); } }
      `}</style>

      <div style={{ display: "flex", minHeight: "100vh", width: "100%" }}>

        {}
        <aside style={{
          width: 260, flexShrink: 0,
          background: "#ffffff",
          borderRight: "1px solid #e2e8f0",
          display: "flex", flexDirection: "column",
          position: "sticky", top: 0, height: "100vh",
          boxShadow: "2px 0 12px rgba(0,0,0,0.04)",
        }}>
          {}
          <div style={{ padding: "26px 20px 22px" }}>
            <div style={{ display: "flex", alignItems: "center", gap: 11 }}>
              <div style={{ background: "linear-gradient(135deg,#1d4ed8,#3b82f6)", borderRadius: 11, padding: "8px 9px", display: "flex", boxShadow: "0 4px 14px rgba(59,130,246,0.35), 0 1px 3px rgba(0,0,0,0.1)", color: "#ffffff" }}>
                <Icon name="film" size={18} />
              </div>
              <div style={{ fontFamily: "'Syne', sans-serif", fontWeight: 800, fontSize: 22, background: "linear-gradient(135deg,#1d4ed8,#3b82f6)", WebkitBackgroundClip: "text", WebkitTextFillColor: "transparent", letterSpacing: "-0.02em", lineHeight: 1.15, transform: "scaleY(1.15)", transformOrigin: "top left", display: "inline-block" }}>Cinema</div>
            </div>
          </div>

          <div style={{ height: 1, background: "#f1f5f9", margin: "0 14px 10px" }} />

          {}
          <nav style={{ flex: 1, padding: "4px 10px" }}>
            {NAV.map(n => {
              const on = active === n.id;
              return (
                <button key={n.id} onClick={() => setActive(n.id)} style={{
                  width: "100%", display: "flex", alignItems: "center", gap: 10,
                  padding: "10px 13px", marginBottom: 2,
                  background: on ? "#eff6ff" : "transparent",
                  border: "none", borderRadius: 10,
                  outline: on ? "1px solid #bfdbfe" : "none",
                  color: on ? "#2563eb" : "#94a3b8",
                  cursor: "pointer", fontSize: 15,
                  fontFamily: "'Inter', sans-serif", fontWeight: on ? 600 : 400,
                  transition: "all 0.13s", textAlign: "left",
                }}
                  onMouseEnter={e => { if (!on) { e.currentTarget.style.color = "#64748b"; e.currentTarget.style.background = "#f8fafc"; } }}
                  onMouseLeave={e => { if (!on) { e.currentTarget.style.color = "#94a3b8"; e.currentTarget.style.background = "transparent"; } }}
                >
                  <Icon name={n.icon} size={14} />
                  {n.label}
                  {on && <span style={{ marginLeft: "auto", width: 6, height: 6, borderRadius: "50%", background: "#3b82f6" }} />}
                </button>
              );
            })}
          </nav>

          <div style={{ padding: "14px 20px", borderTop: "1px solid #f1f5f9" }} />
        </aside>

        {}
        <main style={{
          flex: 1, minWidth: 0, width: 0, padding: "32px 40px",
          background: "#f1f5f9",
          overflow: "hidden",
        }}>
          {pages[active]}
        </main>
      </div>

      <Toast />
    </>
  );
}
