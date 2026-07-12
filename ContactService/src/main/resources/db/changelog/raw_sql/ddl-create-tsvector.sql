ALTER TABLE contact ADD COLUMN search_vector tsvector
GENERATED ALWAYS AS (
    setweight(to_tsvector('simple', COALESCE(name, '')), 'A') ||
    setweight(to_tsvector('simple', COALESCE(phone_number, '')), 'B') ||
    setweight(to_tsvector('simple', COALESCE(email, '')), 'B') ||
    setweight(to_tsvector('simple', COALESCE(company, '')), 'C') ||
    setweight(to_tsvector('simple', COALESCE(note, '')), 'D')
 ) STORED;

CREATE INDEX idx_contacts_search ON contact USING gin(search_vector);