# JKUDE API

Conversor de facturas electrónicas XML a PDF con Quarkus + JasperReports.

### API Endpoints

- `GET /api/facturas` - Lista paginada
- `GET /api/facturas/emisor/{ruc}` - Por RUC emisor
- `GET /api/facturas/receptor/{ruc}` - Por RUC receptor
- `GET /api/facturas/{id}` - Por ID
- `GET /kude/cdc/{cdc}` - Generar PDF por CDC
- `POST /kude/pdf` - Procesar factura

### XML Resource
- `POST /xml/convert` - Convertir XML a JSON

## Uso
```bash
./gradlew quarkusDev
```