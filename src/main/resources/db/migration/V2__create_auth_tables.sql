CREATE TABLE auth.login_attempts (
    id UUID PRIMARY KEY,
    email VARCHAR(320) NOT NULL,
    succeeded BOOLEAN NOT NULL,
    attempted_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX ix_login_attempts_email_attempted_at ON auth.login_attempts (email, attempted_at);

CREATE TABLE auth.refresh_tokens (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    email VARCHAR(320) NOT NULL,
    token_hash VARCHAR(512) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    expires_at TIMESTAMPTZ NOT NULL,
    revoked_at TIMESTAMPTZ
);

CREATE UNIQUE INDEX ix_refresh_tokens_token_hash ON auth.refresh_tokens (token_hash);
CREATE INDEX ix_refresh_tokens_user_id ON auth.refresh_tokens (user_id);
