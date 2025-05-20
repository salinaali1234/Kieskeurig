export class Account {
  id: number;
  name: string | null;
  email: string | null;
  role: string | null;

  constructor(id: number, name: string | null = null) {
    this.id = id;
    this.name = name;
    this.email = null;
    this.role = null;
  }

  static copyConstructor(account: Account | null): Account | null {
    if (!account) return null;
    const copy = new Account(account.id, account.name);
    copy.email = account.email;
    copy.role = account.role;
    return copy;
  }
}
