import { useState, useEffect, useCallback } from "react";

const API = import.meta.env.VITE_API_URL || "";

const api = {
    get: (url) => fetch(`${API}${url}`).then(r => { if (!r.ok) throw new Error(r.statusText); return r.json(); }),
    post: (url, body) => fetch(`${API}${url}`, { method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify(body) }).then(r => { if (!r.ok) throw new Error(r.statusText); return r.json(); }),
    put: (url, body) => fetch(`${API}${url}`, { method: "PUT", headers: { "Content-Type": "application/json" }, body: JSON.stringify(body) }).then(r => { if (!r.ok) throw new Error(r.statusText); return r.json(); }),
    del: (url) => fetch(`${API}${url}`, { method: "DELETE" }).then(r => { if (!r.ok && r.status !== 204) throw new Error(r.statusText); }),
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
const Toast = () => {
    const [toasts, setToasts] = useState([]);
    useEffect(() => {
        toastFn = (msg, type = "success") => {
            const id = Date.now();
            setToasts(p => [...p, { id, msg, type }]);
            setTimeout(() => setToasts(p => p.filter(t => t.id !== id)), 3000);
        };
    }, []);
    return (
        <div style={{ position: "fixed", bottom: 28, right: 28, zIndex: 9999, display: "flex", flexDirection: "column", gap: 10 }}>
            {toasts.map(t => (
                <div key={t.id} style={{
                    background: t.type === "error" ? "#1a0808" : "#081a0f",
                    border: `1px solid ${t.type === "error" ? "#dc2626" : "#16a34a"}`,
                    color: t.type === "error" ? "#fca5a5" : "#86efac",
                    padding: "12px 18px", borderRadius: 10, fontSize: 13, fontFamily: "inherit",
                    animation: "toastIn 0.25s cubic-bezier(.21,1.02,.73,1) forwards",
                    boxShadow: "0 8px 32px rgba(0,0,0,0.5)",
                    maxWidth: 340, display: "flex", alignItems: "center", gap: 10,
                }}>
                    <span>{t.type === "error" ? "✕" : "✓"}</span>{t.msg}
                </div>
            ))}
        </div>
    );
};
const toast = (msg, type) => toastFn?.(msg, type);

const Modal = ({ title, onClose, children }) => (
    <div style={{
        position: "fixed", inset: 0,
        background: "rgba(0,0,0,0.85)", backdropFilter: "blur(6px)",
        zIndex: 1000, display: "flex", alignItems: "center", justifyContent: "center", padding: 20,
    }} onClick={onClose}>
        <div style={{
            background: "#0d1117", border: "1px solid #30363d",
            borderRadius: 16, padding: "28px 32px",
            minWidth: 440, maxWidth: 580, width: "100%",
            maxHeight: "88vh", overflowY: "auto",
            boxShadow: "0 24px 80px rgba(0,0,0,0.8)",
            animation: "modalIn 0.2s cubic-bezier(.21,1.02,.73,1)",
        }} onClick={e => e.stopPropagation()}>
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 26, paddingBottom: 18, borderBottom: "1px solid #21262d" }}>
                <h2 style={{ margin: 0, fontSize: 18, color: "#ffffff", fontFamily: "'Syne', sans-serif", fontWeight: 700, letterSpacing: "-0.01em" }}>{title}</h2>
                <button onClick={onClose} style={{ background: "#21262d", border: "1px solid #30363d", borderRadius: 8, color: "#8b949e", cursor: "pointer", padding: "5px 7px", display: "flex", lineHeight: 1 }}>
                    <Icon name="x" size={14} />
                </button>
            </div>
            {children}
        </div>
    </div>
);

const Field = ({ label, children }) => (
    <div style={{ marginBottom: 18 }}>
        <label style={{ display: "block", fontSize: 11, color: "#94a3b8", marginBottom: 7, textTransform: "uppercase", letterSpacing: "0.09em", fontWeight: 700 }}>{label}</label>
        {children}
    </div>
);

const inputStyle = {
    width: "100%", background: "#010409", border: "1px solid #30363d",
    color: "#ffffff", padding: "10px 13px", borderRadius: 9, fontSize: 13,
    outline: "none", fontFamily: "inherit", boxSizing: "border-box",
};

const btnStyle = (variant = "primary") => ({
    padding: "9px 18px", borderRadius: 9,
    border: variant === "ghost" ? "1px solid #30363d" : "none",
    cursor: "pointer", fontSize: 13, fontWeight: 600, fontFamily: "inherit",
    background: variant === "primary"
        ? "linear-gradient(135deg, #4f46e5, #9333ea)"
        : variant === "danger" ? "#e11d48" : "#21262d",
    color: "#ffffff",
    display: "inline-flex", alignItems: "center", gap: 7,
    whiteSpace: "nowrap",
    transition: "all 0.2s ease",
    boxShadow: variant === "primary" ? "0 4px 14px rgba(79, 70, 229, 0.4)" : "none",
});

const Badge = ({ label }) => (
    <span style={{
        background: "linear-gradient(135deg, #1e1b4b, #312e81)", color: "#ffffff", border: "1px solid #4338ca",
        padding: "2px 10px", borderRadius: 6, fontSize: 11, fontWeight: 700,
        display: "inline-block", marginRight: 5, marginBottom: 5, whiteSpace: "nowrap",
        boxShadow: "0 2px 8px rgba(0,0,0,0.2)",
    }}>{label}</span>
);

const Pagination = ({ meta, onPage }) => {
    if (!meta || meta.totalPage <= 1) return null;
    return (
        <div style={{ display: "flex", alignItems: "center", gap: 10, padding: "14px 20px 6px", justifyContent: "center", borderTop: "1px solid #21262d" }}>
            <button style={{ ...btnStyle("ghost"), padding: "6px 11px" }} disabled={meta.page === 0} onClick={() => onPage(meta.page - 1)}><Icon name="chevLeft" size={13} /></button>
            <span style={{ color: "#94a3b8", fontSize: 12, minWidth: 100, textAlign: "center" }}>
        стр. <b style={{ color: "#ffffff" }}>{meta.page + 1}</b> / <b style={{ color: "#ffffff" }}>{meta.totalPage}</b>
      </span>
            <button style={{ ...btnStyle("ghost"), padding: "6px 11px" }} disabled={meta.page >= meta.totalPage - 1} onClick={() => onPage(meta.page + 1)}><Icon name="chevRight" size={13} /></button>
        </div>
    );
};

const CELL = { padding: "14px 20px", textAlign: "center", verticalAlign: "middle", color: "#ffffff" };

const Table = ({ cols, rows, onEdit, onDelete }) => (
    <div style={{ width: "100%", overflowX: "auto" }}>
        <table style={{
            width: "100%",
            borderCollapse: "collapse",
            fontSize: 15,
            tableLayout: "fixed",
        }}>
            <colgroup>
                {cols.map(c => <col key={c.key} style={{ width: c.width }} />)}
                <col style={{ width: 100 }} />
            </colgroup>

            <thead>
            <tr style={{ background: "#0d1117" }}>
                {cols.map(c => (
                    <th key={c.key} style={{
                        ...CELL,
                        color: "#94a3b8", fontWeight: 700, fontSize: 11,
                        textTransform: "uppercase", letterSpacing: "0.1em",
                        borderBottom: "1px solid #30363d",
                        whiteSpace: "nowrap", overflow: "hidden", textOverflow: "ellipsis",
                    }}>{c.label}</th>
                ))}
                <th style={{
                    ...CELL,
                    color: "#94a3b8", fontWeight: 700, fontSize: 11,
                    textTransform: "uppercase", letterSpacing: "0.1em",
                    borderBottom: "1px solid #30363d", whiteSpace: "nowrap",
                }}>Действия</th>
            </tr>
            </thead>

            <tbody>
            {rows.length === 0 && (
                <tr>
                    <td colSpan={cols.length + 1} style={{ padding: "48px 16px", textAlign: "center", color: "#484f58", fontSize: 13 }}>
                        — нет данных —
                    </td>
                </tr>
            )}
            {rows.map((row, i) => (
                <tr key={row.id ?? i}
                    style={{ borderBottom: "1px solid #21262d", transition: "background 0.1s" }}
                    onMouseEnter={e => e.currentTarget.style.background = "#161b22"}
                    onMouseLeave={e => e.currentTarget.style.background = "transparent"}
                >
                    {cols.map(c => (
                        <td key={c.key} style={{ ...CELL }}>
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
                                    style={{ background: "#21262d", border: "1px solid #30363d", borderRadius: 7, color: "#58a6ff", cursor: "pointer", padding: "6px 8px", display: "flex" }}
                            ><Icon name="edit" size={13} /></button>
                            <button onClick={() => onDelete(row)} title="Удалить"
                                    style={{ background: "#3d1515", border: "1px solid #6e2525", borderRadius: 7, color: "#f85149", cursor: "pointer", padding: "6px 8px", display: "flex" }}
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
                <div style={{ background: "linear-gradient(135deg, #4f46e5, #9333ea)", borderRadius: 10, padding: "8px 9px", display: "flex", boxShadow: "0 4px 15px rgba(79,70,229,0.3)" }}>
                    <span style={{ color: "#ffffff" }}><Icon name={icon} size={17} /></span>
                </div>
                <h2 style={{ margin: 0, fontSize: 28, fontFamily: "'Syne', sans-serif", fontWeight: 800, color: "#ffffff", letterSpacing: "-0.02em" }}>{title}</h2>
            </div>
            <button style={btnStyle()} onClick={onAdd}><Icon name="plus" size={13} />Добавить</button>
        </div>
        <div style={{ background: "#0d1117", border: "1px solid #30363d", borderRadius: 14, overflow: "hidden", boxShadow: "0 4px 40px rgba(0,0,0,0.5)", width: "100%", minWidth: 0 }}>
            {children}
        </div>
    </div>
);

const SearchBar = ({ value, onChange, onSearch, onReset, placeholder }) => (
    <div style={{ display: "flex", gap: 8, padding: "16px 16px 0" }}>
        <div style={{ position: "relative", flex: 1, maxWidth: 300 }}>
      <span style={{ position: "absolute", left: 11, top: "50%", transform: "translateY(-50%)", color: "#484f58", pointerEvents: "none" }}>
        <Icon name="search" size={14} />
      </span>
            <input style={{ ...inputStyle, paddingLeft: 34 }} value={value} onChange={e => onChange(e.target.value)}
                   placeholder={placeholder} onKeyDown={e => e.key === "Enter" && onSearch()} />
        </div>
        <button style={btnStyle()} onClick={onSearch}><Icon name="search" size={13} />Найти</button>
        {onReset && <button style={btnStyle("ghost")} onClick={onReset}>Сбросить</button>}
    </div>
);

const CardDivider = () => <div style={{ height: 1, background: "#21262d", margin: "14px 0 0" }} />;

const ModalActions = ({ onCancel, onSave }) => (
    <div style={{ display: "flex", gap: 10, justifyContent: "flex-end", marginTop: 24, paddingTop: 18, borderTop: "1px solid #21262d" }}>
        <button style={btnStyle("ghost")} onClick={onCancel}>Отмена</button>
        <button style={btnStyle()} onClick={onSave}>Сохранить</button>
    </div>
);

const Genres = () => {
    const [data, setData] = useState([]);
    const [modal, setModal] = useState(null);
    const [form, setForm] = useState({ name: "" });
    const load = useCallback(() => api.get("/genres").then(setData).catch(() => toast("Ошибка загрузки жанров", "error")), []);
    useEffect(() => { load(); }, [load]);
    const openCreate = () => { setForm({ name: "" }); setModal({ mode: "create" }); };
    const openEdit = (item) => { setForm({ name: item.name }); setModal({ mode: "edit", item }); };
    const submit = async () => {
        try {
            if (modal.mode === "create") await api.post("/genres/post", form);
            else await api.put(`/genres/update/${modal.item.id}`, form);
            toast(modal.mode === "create" ? "Жанр создан" : "Жанр обновлён"); setModal(null); load();
        } catch { toast("Ошибка сохранения", "error"); }
    };
    const del = async (item) => {
        if (!confirm(`Удалить жанр «${item.name}»?`)) return;
        try { await api.del(`/genres/delete/${item.id}`); toast("Удалено"); load(); } catch { toast("Ошибка удаления", "error"); }
    };
    return (
        <Section title="Жанры" icon="tag" onAdd={openCreate}>
            <Table cols={[{ key: "name", label: "Название", width: "100%" }]} rows={data} onEdit={openEdit} onDelete={del} />
            {modal && (<Modal title={modal.mode === "create" ? "Новый жанр" : "Редактировать жанр"} onClose={() => setModal(null)}>
                <Field label="Название"><input style={inputStyle} value={form.name} onChange={e => setForm({ name: e.target.value })} placeholder="Боевик" maxLength={20} /></Field>
                <ModalActions onCancel={() => setModal(null)} onSave={submit} />
            </Modal>)}
        </Section>
    );
};

const Halls = () => {
    const [data, setData] = useState([]);
    const [modal, setModal] = useState(null);
    const [form, setForm] = useState({ name: "", price: "", seatAmount: "" });
    const load = useCallback(() => api.get("/halls").then(setData).catch(() => toast("Ошибка загрузки залов", "error")), []);
    useEffect(() => { load(); }, [load]);
    const openCreate = () => { setForm({ name: "", price: "", seatAmount: "" }); setModal({ mode: "create" }); };
    const openEdit = (item) => { setForm({ name: item.name, price: item.price, seatAmount: item.seatAmount }); setModal({ mode: "edit", item }); };
    const submit = async () => {
        try {
            const body = { name: form.name, price: Number(form.price), seatAmount: Number(form.seatAmount) };
            if (modal.mode === "create") await api.post("/halls/post", body); else await api.put(`/halls/update/${modal.item.id}`, body);
            toast("Сохранено"); setModal(null); load();
        } catch { toast("Ошибка сохранения", "error"); }
    };
    const del = async (item) => {
        if (!confirm(`Удалить зал «${item.name}»?`)) return;
        try { await api.del(`/halls/delete/${item.id}`); toast("Удалено"); load(); } catch { toast("Ошибка удаления", "error"); }
    };
    return (
        <Section title="Залы" icon="building" onAdd={openCreate}>
            <Table cols={[
                { key: "name", label: "Название", width: "100%" },
                { key: "price", label: "Цена", width: 120, render: r => <span style={{ color: "#ffffff", fontWeight: 700 }}>{r.price} р.</span> },
                { key: "seatAmount", label: "Мест", width: 100 },
            ]} rows={data} onEdit={openEdit} onDelete={del} />
            {modal && (<Modal title={modal.mode === "create" ? "Новый зал" : "Редактировать зал"} onClose={() => setModal(null)}>
                <Field label="Название"><input style={inputStyle} value={form.name} onChange={e => setForm(p => ({ ...p, name: e.target.value }))} placeholder="Зал 1" maxLength={20} /></Field>
                <Field label="Цена (₽)"><input style={inputStyle} type="number" value={form.price} onChange={e => setForm(p => ({ ...p, price: e.target.value }))} placeholder="350" /></Field>
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
    const [modal, setModal] = useState(null);
    const [genres, setGenres] = useState([]);
    const [form, setForm] = useState({ title: "", duration: "", genreIds: [] });
    const load = useCallback(() =>
        api.get(`/movies?page=${page}&size=10`).then(r => { setData(r.content); setMeta(r.metadata); }).catch(() => toast("Ошибка загрузки фильмов", "error")), [page]);
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
            toast("Сохранено"); setModal(null); load();
        } catch { toast("Ошибка сохранения", "error"); }
    };
    const del = async (item) => {
        if (!confirm(`Удалить фильм «${item.title}»?`)) return;
        try { await api.del(`/movies/delete/${item.id}`); toast("Удалено"); load(); } catch { toast("Ошибка удаления", "error"); }
    };
    return (
        <Section title="Фильмы" icon="film" onAdd={openCreate}>
            <Table cols={[
                { key: "title", label: "Название", width: 240 },
                { key: "duration", label: "Длительность", width: 140, render: r => <span>{r.duration} мин</span> },
                { key: "genres", label: "Жанры", width: "100%", render: r => <div style={{ display: "flex", flexWrap: "wrap", gap: 3, justifyContent: "center" }}>{(r.genres || []).map(g => <Badge key={g} label={g} />)}</div> },
            ]} rows={data} onEdit={openEdit} onDelete={del} />
            <Pagination meta={meta} onPage={setPage} />
            {modal && (<Modal title={modal.mode === "create" ? "Новый фильм" : "Редактировать фильм"} onClose={() => setModal(null)}>
                <Field label="Название"><input style={inputStyle} value={form.title} onChange={e => setForm(p => ({ ...p, title: e.target.value }))} placeholder="Название фильма" maxLength={50} /></Field>
                <Field label="Длительность (мин)"><input style={inputStyle} type="number" value={form.duration} onChange={e => setForm(p => ({ ...p, duration: e.target.value }))} placeholder="120" /></Field>
                <Field label="Жанры (ManyToMany)">
                    <div style={{ display: "flex", flexWrap: "wrap", gap: 8, padding: "10px 14px", background: "#010409", border: "1px solid #30363d", borderRadius: 9 }}>
                        {genres.length === 0 && <span style={{ color: "#484f58", fontSize: 12 }}>Нет жанров</span>}
                        {genres.map(g => (
                            <label key={g.id} style={{ display: "flex", alignItems: "center", gap: 7, cursor: "pointer", color: form.genreIds.includes(g.id) ? "#818cf8" : "#94a3b8", fontSize: 13, userSelect: "none" }}>
                                <input type="checkbox" checked={form.genreIds.includes(g.id)} onChange={() => toggleGenre(g.id)} style={{ accentColor: "#6366f1" }} />{g.name}
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
    const [modal, setModal] = useState(null);
    const [movies, setMovies] = useState([]);
    const [halls, setHalls] = useState([]);
    const [search, setSearch] = useState("");
    const [searchInput, setSearchInput] = useState("");
    const [form, setForm] = useState({ startTime: "", movieId: "", hallId: "" });
    const load = useCallback(() => {
        const url = search ? `/sessions/search?title=${encodeURIComponent(search)}&page=${page}&size=10` : `/sessions?page=${page}&size=10`;
        api.get(url).then(r => { setData(r.content); setMeta(r.metadata); }).catch(() => toast("Ошибка загрузки сеансов", "error"));
    }, [page, search]);
    useEffect(() => { load(); }, [load]);
    useEffect(() => { api.get("/movies?size=100").then(r => setMovies(r.content)).catch(() => {}); api.get("/halls").then(setHalls).catch(() => {}); }, []);
    const openCreate = () => { setForm({ startTime: "", movieId: "", hallId: "" }); setModal({ mode: "create" }); };
    const openEdit = (item) => {
        const movie = movies.find(m => m.title === item.movieTitle);
        const hall = halls.find(h => h.name === item.hallName);
        const dt = item.startTime ? item.startTime.replace(" ", "T").slice(0, 16) : "";
        setForm({ startTime: dt, movieId: movie?.id ?? "", hallId: hall?.id ?? "" }); setModal({ mode: "edit", item });
    };
    const submit = async () => {
        try {
            const body = { startTime: form.startTime + ":00", movieId: Number(form.movieId), hallId: Number(form.hallId) };
            if (modal.mode === "create") await api.post("/sessions/post", body); else await api.put(`/sessions/update/${modal.item.id}`, body);
            toast("Сохранено"); setModal(null); load();
        } catch { toast("Ошибка сохранения", "error"); }
    };
    const del = async (item) => {
        if (!confirm("Удалить сеанс?")) return;
        try { await api.del(`/sessions/delete/${item.id}`); toast("Удалено"); load(); } catch { toast("Ошибка удаления", "error"); }
    };
    const handleSearch = () => { setSearch(searchInput); setPage(0); };
    return (
        <Section title="Сеансы" icon="clock" onAdd={openCreate}>
            <SearchBar value={searchInput} onChange={setSearchInput} onSearch={handleSearch}
                       onReset={search ? () => { setSearch(""); setSearchInput(""); setPage(0); } : null} placeholder="Поиск по фильму..." />
            <CardDivider />
            <Table cols={[
                { key: "startTime", label: "Начало", width: 120, render: r => {
                        const dt = r.startTime?.replace("T", " ").slice(0, 16) ?? "";
                        const [date, time] = dt.split(" ");
                        return <div style={{ fontVariantNumeric: "tabular-nums", lineHeight: 1.5 }}>
                            <div style={{ color: "#ffffff", fontWeight: 600 }}>{time}</div>
                            <div style={{ color: "#94a3b8", fontSize: 11 }}>{date}</div>
                        </div>;
                    }},
                { key: "movieTitle", label: "Фильм", width: "100%" },
                { key: "hallName", label: "Зал", width: 140 },
                { key: "price", label: "Цена", width: 100, render: r => <span style={{ color: "#ffffff", fontWeight: 700 }}>{r.price} р.</span> },
            ]} rows={data} onEdit={openEdit} onDelete={del} />
            <Pagination meta={meta} onPage={setPage} />
            {modal && (<Modal title={modal.mode === "create" ? "Новый сеанс" : "Редактировать сеанс"} onClose={() => setModal(null)}>
                <Field label="Дата и время"><input style={inputStyle} type="datetime-local" value={form.startTime} onChange={e => setForm(p => ({ ...p, startTime: e.target.value }))} /></Field>
                <Field label="Фильм"><select style={inputStyle} value={form.movieId} onChange={e => setForm(p => ({ ...p, movieId: e.target.value }))}><option value="">— выбрать —</option>{movies.map(m => <option key={m.id} value={m.id}>{m.title}</option>)}</select></Field>
                <Field label="Зал"><select style={inputStyle} value={form.hallId} onChange={e => setForm(p => ({ ...p, hallId: e.target.value }))}><option value="">— выбрать —</option>{halls.map(h => <option key={h.id} value={h.id}>{h.name} ({h.price} р.)</option>)}</select></Field>
                <ModalActions onCancel={() => setModal(null)} onSave={submit} />
            </Modal>)}
        </Section>
    );
};

const Visitors = () => {
    const [data, setData] = useState([]);
    const [modal, setModal] = useState(null);
    const [form, setForm] = useState({ name: "", email: "" });
    const load = useCallback(() => api.get("/visitors").then(setData).catch(() => toast("Ошибка загрузки посетителей", "error")), []);
    useEffect(() => { load(); }, [load]);
    const openCreate = () => { setForm({ name: "", email: "" }); setModal({ mode: "create" }); };
    const openEdit = (item) => { setForm({ name: item.name, email: item.email }); setModal({ mode: "edit", item }); };
    const submit = async () => {
        try {
            if (modal.mode === "create") await api.post("/visitors/post", form); else await api.put(`/visitors/update/${modal.item.id}`, form);
            toast("Сохранено"); setModal(null); load();
        } catch { toast("Ошибка сохранения", "error"); }
    };
    const del = async (item) => {
        if (!confirm(`Удалить посетителя «${item.name}»?`)) return;
        try { await api.del(`/visitors/delete/${item.id}`); toast("Удалено"); load(); } catch { toast("Ошибка удаления", "error"); }
    };
    return (
        <Section title="Посетители" icon="user" onAdd={openCreate}>
            <Table cols={[
                { key: "name", label: "Имя", width: 200 },
                { key: "email", label: "Email", width: "100%" },
            ]} rows={data} onEdit={openEdit} onDelete={del} />
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
    const [modal, setModal] = useState(null);
    const [sessions, setSessions] = useState([]);
    const [visitors, setVisitors] = useState([]);
    const [search, setSearch] = useState("");
    const [searchInput, setSearchInput] = useState("");
    const [form, setForm] = useState({ sessionId: "", visitorId: "", seatNumber: "" });
    const load = useCallback(() => {
        const url = search ? `/tickets/search?visitorName=${encodeURIComponent(search)}&page=${page}&size=10` : `/tickets?page=${page}&size=10`;
        api.get(url).then(r => { setData(r.content); setMeta(r.metadata); }).catch(() => toast("Ошибка загрузки билетов", "error"));
    }, [page, search]);
    useEffect(() => { load(); }, [load]);
    useEffect(() => { api.get("/sessions?size=100").then(r => setSessions(r.content)).catch(() => {}); api.get("/visitors").then(setVisitors).catch(() => {}); }, []);
    const openCreate = () => { setForm({ sessionId: "", visitorId: "", seatNumber: "" }); setModal({ mode: "create" }); };
    const openEdit = (item) => {
        const session = sessions.find(s => s.id === item.session?.id);
        const visitor = visitors.find(v => v.email === item.visitorEmail);
        setForm({ sessionId: session?.id ?? "", visitorId: visitor?.id ?? "", seatNumber: item.seatNumber }); setModal({ mode: "edit", item });
    };
    const submit = async () => {
        try {
            const body = { sessionId: Number(form.sessionId), visitorId: Number(form.visitorId), seatNumber: Number(form.seatNumber) };
            if (modal.mode === "create") await api.post("/tickets/post", body); else await api.put(`/tickets/update/${modal.item.id}`, body);
            toast("Сохранено"); setModal(null); load();
        } catch { toast("Ошибка сохранения", "error"); }
    };
    const del = async (item) => {
        if (!confirm("Удалить билет?")) return;
        try { await api.del(`/tickets/delete/${item.id}`); toast("Удалено"); load(); } catch { toast("Ошибка удаления", "error"); }
    };
    const handleSearch = () => { setSearch(searchInput); setPage(0); };
    return (
        <Section title="Билеты" icon="ticket" onAdd={openCreate}>
            <SearchBar value={searchInput} onChange={setSearchInput} onSearch={handleSearch}
                       onReset={search ? () => { setSearch(""); setSearchInput(""); setPage(0); } : null} placeholder="Поиск по посетителю..." />
            <CardDivider />
            <Table cols={[
                { key: "seatNumber", label: "Место", width: 100 },
                { key: "session", label: "Фильм", width: 220, render: r => r.session
                        ? <span style={{ color: "#ffffff" }}>{r.session.movieTitle}</span>
                        : "—"
                },
                { key: "sessionTime", label: "Дата сеанса", width: 130, render: r => {
                        const dt = r.session?.startTime?.slice(0, 16).replace("T", " ") ?? "";
                        const [date, time] = dt.split(" ");
                        return dt ? <div style={{ fontVariantNumeric: "tabular-nums", lineHeight: 1.5 }}>
                            <div style={{ color: "#ffffff", fontWeight: 600 }}>{time}</div>
                            <div style={{ color: "#94a3b8", fontSize: 11 }}>{date}</div>
                        </div> : <span style={{ color: "#484f58" }}>—</span>;
                    }},
                { key: "visitorEmail", label: "Посетитель", width: "100%" },
            ]} rows={data} onEdit={openEdit} onDelete={del} />
            <Pagination meta={meta} onPage={setPage} />
            {modal && (<Modal title={modal.mode === "create" ? "Новый билет" : "Редактировать билет"} onClose={() => setModal(null)}>
                <Field label="Сеанс"><select style={inputStyle} value={form.sessionId} onChange={e => setForm(p => ({ ...p, sessionId: e.target.value }))}><option value="">— выбрать —</option>{sessions.map(s => <option key={s.id} value={s.id}>{s.movieTitle} | {s.startTime?.slice(0, 16).replace("T", " ")} | {s.hallName}</option>)}</select></Field>
                <Field label="Посетитель"><select style={inputStyle} value={form.visitorId} onChange={e => setForm(p => ({ ...p, visitorId: e.target.value }))}><option value="">— выбрать —</option>{visitors.map(v => <option key={v.id} value={v.id}>{v.name} ({v.email})</option>)}</select></Field>
                <Field label="Номер места"><input style={inputStyle} type="number" value={form.seatNumber} onChange={e => setForm(p => ({ ...p, seatNumber: e.target.value }))} placeholder="1" /></Field>
                <ModalActions onCancel={() => setModal(null)} onSave={submit} />
            </Modal>)}
        </Section>
    );
};

const NAV = [
    { id: "genres",   label: "Жанры",      icon: "tag" },
    { id: "halls",    label: "Залы",        icon: "building" },
    { id: "movies",   label: "Фильмы",     icon: "film" },
    { id: "sessions", label: "Сеансы",     icon: "clock" },
    { id: "visitors", label: "Посетители", icon: "user" },
    { id: "tickets",  label: "Билеты",     icon: "ticket" },
];

export default function App() {
    const [active, setActive] = useState("genres");
    const pages = { genres: <Genres />, halls: <Halls />, movies: <Movies />, sessions: <Sessions />, visitors: <Visitors />, tickets: <Tickets /> };

    return (
        <>
            <style>{`
        @import url('https://fonts.googleapis.com/css2?family=Syne:wght@700;800&family=Inter:wght@400;500;600&display=swap');
        *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
        html, body, #root { width: 100%; min-height: 100vh; font-size: 16px; }
        body { background: #010409; color: #ffffff; font-family: 'Inter', sans-serif; -webkit-font-smoothing: antialiased; }
        ::-webkit-scrollbar { width: 6px; height: 6px; }
        ::-webkit-scrollbar-track { background: transparent; }
        ::-webkit-scrollbar-thumb { background: #30363d; border-radius: 10px; }
        select option { background: #0d1117; color: #ffffff; }
        input[type="datetime-local"]::-webkit-calendar-picker-indicator { filter: invert(1); }
        input:focus, select:focus { border-color: #6366f1 !important; box-shadow: 0 0 0 4px rgba(99,102,241,0.25); }
        @keyframes toastIn { from { opacity:0; transform:translateY(20px); } to { opacity:1; transform:translateY(0); } }
        @keyframes modalIn { from { opacity:0; transform:scale(0.95); } to { opacity:1; transform:scale(1); } }
      `}</style>

            <div style={{ display: "flex", minHeight: "100vh", width: "100%" }}>

                {/* Sidebar */}
                <aside style={{
                    width: 260, flexShrink: 0,
                    background: "#0d1117",
                    borderRight: "1px solid #30363d",
                    display: "flex", flexDirection: "column",
                    position: "sticky", top: 0, height: "100vh",
                }}>
                    {/* Logo */}
                    <div style={{ padding: "26px 20px 22px" }}>
                        <div style={{ display: "flex", alignItems: "center", gap: 11 }}>
                            <div style={{ background: "linear-gradient(135deg, #6366f1, #a855f7)", borderRadius: 12, padding: "8px 9px", display: "flex", boxShadow: "0 4px 20px rgba(99,102,241,0.4)" }}>
                                <Icon name="film" size={18} />
                            </div>
                            <div>
                                <div style={{ fontFamily: "'Syne', sans-serif", fontWeight: 800, fontSize: 20, color: "#ffffff", letterSpacing: "-0.02em" }}>Cinema</div>
                            </div>
                        </div>
                    </div>

                    <div style={{ height: 1, background: "#21262d", margin: "0 14px 10px" }} />

                    {/* Nav */}
                    <nav style={{ flex: 1, padding: "4px 10px" }}>
                        {NAV.map(n => {
                            const on = active === n.id;
                            return (
                                <button key={n.id} onClick={() => setActive(n.id)} style={{
                                    width: "100%", display: "flex", alignItems: "center", gap: 10,
                                    padding: "12px 14px", marginBottom: 4,
                                    background: on ? "linear-gradient(90deg, #1e1b4b 0%, #312e81 100%)" : "transparent",
                                    border: "none", borderRadius: 12,
                                    outline: on ? "1px solid #4338ca" : "none",
                                    color: on ? "#ffffff" : "#8b949e",
                                    cursor: "pointer", fontSize: 15,
                                    fontFamily: "'Inter', sans-serif", fontWeight: on ? 600 : 500,
                                    transition: "all 0.2s", textAlign: "left",
                                }}
                                        onMouseEnter={e => { if (!on) { e.currentTarget.style.color = "#ffffff"; e.currentTarget.style.background = "#161b22"; } }}
                                        onMouseLeave={e => { if (!on) { e.currentTarget.style.color = "#8b949e"; e.currentTarget.style.background = "transparent"; } }}
                                >
                                    <Icon name={n.icon} size={16} />
                                    {n.label}
                                    {on && <span style={{ marginLeft: "auto", width: 6, height: 6, borderRadius: "50%", background: "#818cf8", boxShadow: "0 0 10px #818cf8" }} />}
                                </button>
                            );
                        })}
                    </nav>
                </aside>

                {/* Main */}
                <main style={{
                    flex: 1, minWidth: 0, width: 0, padding: "32px 40px",
                    background: "radial-gradient(circle at 50% 0%, #1e1b4b 0%, #010409 70%)",
                    overflow: "hidden",
                }}>
                    {pages[active]}
                </main>
            </div>

            <Toast />
        </>
    );
}